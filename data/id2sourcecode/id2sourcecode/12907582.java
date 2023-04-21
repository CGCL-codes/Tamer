    public void handleRequest(Message message) {
        String format = "^#(\\d+)\\s+(.*)x\\s+\\[\\s*(.*[KMGB])\\]\\s+(.*)";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(message.getMessage());
        if (matcher.find()) {
            String packText = matcher.group(4).trim().replace("'", "\\'");
            Database db = Database.getInstance();
            String query = "INSERT INTO `packets` VALUES (null, null, '" + message.getNetwork() + "', '" + message.getChannel() + "', '" + message.getSender() + "', " + matcher.group(1) + ", " + matcher.group(2) + ", '" + matcher.group(3) + "', '" + packText + "')";
            if (!db.insert(query)) System.err.println("Insert error: " + packText);
        } else {
            super.handleRequest(message);
        }
    }
