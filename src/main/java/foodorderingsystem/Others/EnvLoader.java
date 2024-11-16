package foodorderingsystem.Others;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {
    private static final String ENV_FILE_PATH = "src\\main\\java\\foodorderingsystem\\Others\\.env";
    private static Map<String, String> envVariables = new HashMap<>();

    static {
        loadEnvVariables();
    }

    private static void loadEnvVariables() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ENV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    envVariables.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading .env file: " + e.getMessage());
        }
    }

    public static String getEnv(String key) {
        return envVariables.get(key);
    }
}