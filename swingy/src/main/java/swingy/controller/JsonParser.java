package swingy.controller;

public class JsonParser {
    public static class JsonStats {
        public String className;
        public int level;
        public int health;
        public int attackPower;
        public int defense;

        public JsonStats(String className, int level, int health, int attackPower, int defense) {
            this.className = className;
            this.level = level;
            this.health = health;
            this.attackPower = attackPower;
            this.defense = defense;
        }
    }

    public static JsonStats parseJsonStats(String json) {
        try {
            // Find start of JSON object
            int start = json.indexOf('{');
            int end = json.lastIndexOf('}');
            if (start == -1 || end == -1 || start >= end) return null;

            String content = json.substring(start + 1, end).trim();
            String[] pairs = content.split(",(?=\\s*[^\\s]*\\s*:)");

            String className = "Unknown";
            int level = 0, health = 0, attackPower = 0, defense = 0;

            for (String pair : pairs) {
                pair = pair.trim();
                if (pair.startsWith("\"heroClass\"")) {
                    className = extractValue(pair).replace("\"", "");
                } else if (pair.startsWith("\"level\"")) {
                    level = Integer.parseInt(extractValue(pair));
                } else if (pair.startsWith("\"baseHP\"")) {
                    health = Integer.parseInt(extractValue(pair));
                } else if (pair.startsWith("\"attack\"")) {
                    attackPower = Integer.parseInt(extractValue(pair));
                } else if (pair.startsWith("\"defense\"")) {
                    defense = Integer.parseInt(extractValue(pair));
                }
            }

            return new JsonStats(className, level, health, attackPower, defense);
        } catch (Exception e) {
            System.err.println("Failed to parse JSON: " + e.getMessage());
            return null;
        }
    }

    private static String extractValue(String pair) {
        int colonIndex = pair.indexOf(':');
        if (colonIndex != -1) {
            return pair.substring(colonIndex + 1).trim();
        }
        return "";
    }
}
