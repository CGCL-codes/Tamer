    public void sendLargePostRequest(final int size) throws IOException {
        String encodedData = URLEncoder.encode("test", WebCastellumFilter.DEFAULT_CHARACTER_ENCODING) + "=" + URLEncoder.encode("this is just a mass test", WebCastellumFilter.DEFAULT_CHARACTER_ENCODING);
        encodedData += "&" + URLEncoder.encode("payload", WebCastellumFilter.DEFAULT_CHARACTER_ENCODING) + "=" + createTestdata(size);
        final long start = System.currentTimeMillis();
        final long end;
        HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setFollowRedirects(true);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", "" + encodedData.length());
        DataOutputStream output = null;
        BufferedReader reader = null;
        try {
            output = new DataOutputStream(connection.getOutputStream());
            output.writeBytes(encodedData);
            output.flush();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
            }
            end = System.currentTimeMillis();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ignored) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("Duration: " + (end - start) + " ms");
    }
