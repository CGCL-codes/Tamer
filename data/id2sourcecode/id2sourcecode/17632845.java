    public static void sendHttpPost(String target, Map<String, String> context, int desiredResponseCode) throws IOException {
        int retry = ApplicationProperty.HTTP_RETRY_TIMES;
        while (true) try {
            URL url = new URL(target);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            boolean isFirst = true;
            for (Map.Entry<String, String> entry : context.entrySet()) {
                if (isFirst) isFirst = false; else writer.write('&');
                writer.write(URLEncoder.encode(entry.getKey(), "UTF-8"));
                writer.write('=');
                writer.write(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            writer.close();
            if (connection.getResponseCode() == desiredResponseCode) return; else throw new IOException();
        } catch (IOException e) {
            retry--;
            if (retry == 0) throw e;
        }
    }
