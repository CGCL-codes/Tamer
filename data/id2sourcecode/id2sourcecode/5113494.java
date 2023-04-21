    private String doCommand(String command, String type) throws IOException {
        StringBuilder url = new StringBuilder(commandUrl);
        url.append(command);
        if (type != null) {
            url.append("?type=").append(type);
        }
        HttpURLConnection connection = (HttpURLConnection) new URL(url.toString()).openConnection();
        try {
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_ACCEPTED) {
                throw new IOException("request failed.  responseCode=[" + responseCode + "]");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            try {
                StringWriter writer = new StringWriter();
                char[] buff = new char[128];
                int length;
                while ((length = reader.read(buff)) != -1) {
                    writer.write(buff, 0, length);
                }
                return writer.toString();
            } finally {
                reader.close();
            }
        } finally {
            connection.disconnect();
        }
    }
