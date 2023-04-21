    public static InputStream doPost(URL url, Map<String, Object> parameters) throws java.io.IOException {
        final int RADIX = 36;
        final String SEPARATOR = "--";
        final String BOUNDARY = Long.toString(random.nextLong(), RADIX);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", url.getHost());
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Accept-Language", "en-us");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Cookie", "JSESSIONID=EBF7C6DE115A0A49F50FFF37263FA62D");
            connection.setChunkedStreamingMode(8000);
            connection.setDoOutput(true);
            final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            for (Iterator<Map.Entry<String, Object>> i = parameters.entrySet().iterator(); i.hasNext(); ) {
                final Map.Entry<String, Object> entry = i.next();
                Object value = null;
                writer.write(SEPARATOR);
                writer.write(BOUNDARY);
                writer.newLine();
                writer.write("Content-Disposition: form-data; name=\"");
                writer.write(entry.getKey());
                writer.write("\"");
                if (entry.getValue() instanceof File) {
                    final File file = (File) entry.getValue();
                    writer.write("; filename=\"");
                    writer.write(file.getName());
                    writer.write("\"");
                    writer.newLine();
                    writer.write("Content-Type:");
                    String contentType = URLConnection.guessContentTypeFromName(file.getPath());
                    if (contentType == null) {
                        contentType = "text/plain";
                    }
                    writer.write(contentType);
                    value = new String(new FileUtils().read(file.toURI().toURL()));
                } else {
                    value = entry.getValue();
                }
                writer.newLine();
                writer.newLine();
                writer.write(value.toString());
                writer.newLine();
            }
            writer.write(SEPARATOR);
            writer.write(BOUNDARY);
            writer.write(SEPARATOR);
            writer.newLine();
            writer.close();
            return connection.getInputStream();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
