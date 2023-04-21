    public static HttpURLConnection doPostMultipart(final URL url, final Map<String, Object> parameters) throws IOException {
        final int RADIX = 36;
        final String SEPARATOR = "--";
        final String BOUNDARY = Long.toString(random.nextLong(), RADIX);
        final String NEW_LINE = "\r\n";
        BufferedWriter writer = null;
        try {
            final HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setRequestProperty("Host", url.getHost());
            http.setRequestProperty("Content-Type", "multipart/form-data, boundary=" + BOUNDARY);
            http.setRequestProperty("Cache-Control", "no-cache");
            http.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            http.setDoOutput(true);
            writer = new BufferedWriter(new OutputStreamWriter(http.getOutputStream()));
            for (Iterator<Map.Entry<String, Object>> i = parameters.entrySet().iterator(); i.hasNext(); ) {
                final Map.Entry<String, Object> entry = i.next();
                Object value = null;
                writer.write(SEPARATOR);
                writer.write(BOUNDARY);
                writer.write(NEW_LINE);
                writer.write("Content-Disposition: form-data; name=\"");
                writer.write(entry.getKey());
                writer.write("\"");
                if (entry.getValue() instanceof File) {
                    final File file = (File) entry.getValue();
                    writer.write("; filename=\"");
                    writer.write(file.getName());
                    writer.write("\"");
                    writer.write(NEW_LINE);
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
                writer.write(NEW_LINE);
                writer.write(NEW_LINE);
                writer.write(value.toString());
                writer.write(NEW_LINE);
            }
            writer.write(SEPARATOR);
            writer.write(BOUNDARY);
            writer.write(SEPARATOR);
            writer.write(NEW_LINE);
            writer.close();
            return http;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
