    public static String sanitizeHTML(String html) throws IllegalOperationException {
        try {
            String data = "content=" + URLEncoder.encode(html, "UTF-8");
            URL url = new URL(Config.getSanitizer());
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(data);
            writer.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null) buffer.append(line);
            writer.close();
            reader.close();
            return buffer.toString();
        } catch (Exception e) {
            LoggingSystem.getServerLogger().warn("Could not sanitize message.", e);
            throw new IllegalOperationException("Impossible de parser le message.");
        }
    }
