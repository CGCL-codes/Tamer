    public void parse(java.io.InputStream data, Crawler crawler, String site) {
        try {
            java.io.ByteArrayOutputStream bytes = new java.io.ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int num_read = 0;
            while ((num_read = data.read(buffer)) > 0) {
                bytes.write(buffer, 0, num_read);
            }
            java.io.ByteArrayInputStream source = new java.io.ByteArrayInputStream(bytes.toByteArray());
            parse(source, site);
            source.mark(0);
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(source));
            StringBuffer content = new StringBuffer();
            String line = reader.readLine();
            while ((line != null) && (!line.contentEquals(""))) {
                content.append(line).append(" ");
                line = reader.readLine();
            }
            processLinks(content.toString(), crawler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
