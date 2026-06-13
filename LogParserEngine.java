import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

public class LogParserEngine {
    
    // Highly optimized Regex patterns to mask dynamic variables
    private static final Pattern TIMESTAMP_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}[T ]\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?Z?");
    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(?:\\.\\d{1,3}){3}");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("\\d+");

    public static class ClusterMeta {
        String severity;
        int count;
        String template;

        public ClusterMeta(String severity, String template) {
            this.severity = severity;
            this.count = 1;
            this.template = template;
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java LogParserEngine <path-to-log-file>");
            return;
        }

        String inputFilePath = args[0];
        String outputFilePath = "summary.json";

        long startTime = System.currentTimeMillis();
        int totalLines = 0;
        int errorCount = 0;
        int warnCount = 0;

        Map<String, ClusterMeta> clusters = new LinkedHashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                totalLines++;

                // Determine Log Severity Level
                String severity = "INFO";
                String upperLine = line.toUpperCase();
                if (upperLine.contains("ERROR") || upperLine.contains("FATAL") || upperLine.contains("CRITICAL")) {
                    severity = "ERROR";
                    errorCount++;
                } else if (upperLine.contains("WARN") || upperLine.contains("WARNING")) {
                    severity = "WARN";
                    warnCount++;
                }

                // Execute Tokenization Pipeline
                String masked = TIMESTAMP_PATTERN.matcher(line).replaceAll("[TIMESTAMP]");
                masked = UUID_PATTERN.matcher(masked).replaceAll("[UUID]");
                masked = IP_PATTERN.matcher(masked).replaceAll("[IP_ADDRESS]");
                masked = NUMERIC_PATTERN.matcher(masked).replaceAll("[NUM]");

                // Aggregation Layer
                if (clusters.containsKey(masked)) {
                    clusters.get(masked).count++;
                } else {
                    clusters.put(masked, new ClusterMeta(severity, masked));
                }
            }
        } catch (IOException e) {
            System.err.println("Fatal Error reading the log file: " + e.getMessage());
            return;
        }

        long endTime = System.currentTimeMillis();
        long latency = endTime - startTime;
        int healthIndex = totalLines > 0 ? Math.max(0, Math.min(100, ((totalLines - errorCount) * 100) / totalLines)) : 100;

        // Export data to raw structured JSON format with proper array formatting
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
            writer.println("{");
            writer.println("  \"totalLines\": " + totalLines + ",");
            writer.println("  \"latencyMs\": " + latency + ",");
            writer.println("  \"uniqueClusters\": " + clusters.size() + ",");
            writer.println("  \"healthIndex\": " + healthIndex + ",");
            writer.println("  \"clusters\": [");
            
            int index = 0;
            for (ClusterMeta meta : clusters.values()) {
                writer.println("    {");
                writer.println("      \"severity\": \"\"" + meta.severity + "\",");
                writer.println("      \"count\": " + meta.count + ",");
                writer.println("      \"pattern\": \"" + escapeJson(meta.template) + "\"");
                
                // FIXED: Now accurately writes to the json file stream instead of system terminal console
                writer.print(++index == clusters.size() ? "    }\n" : "    },\n");
            }
            writer.println("  ]");
            writer.println("}");
            System.out.println("[✓] Execution Complete. Structural telemetry saved to 'summary.json' in " + latency + "ms.");
        } catch (IOException e) {
            System.err.println("Error writing telemetry JSON file: " + e.getMessage());
        }
    }

    private static String escapeJson(String input) {
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\b", "\\b")
                    .replace("\f", "\\f")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
