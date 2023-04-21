    private void sendFile(URL url, File file) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.addRequestProperty("Content-Type", "text/xml");
            connection.connect();
            InputStream fileIn = new FileInputStream(file);
            OutputStream urlOut = connection.getOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int count = fileIn.read(buffer);
            while (count >= 0) {
                urlOut.write(buffer, 0, count);
                count = fileIn.read(buffer);
            }
            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            fileIn.close();
            urlOut.close();
            connection.disconnect();
            if (responseCode == 200) {
                out.println(file.getName());
            } else {
                err.println(file.getName() + " (" + responseMessage + ")");
            }
        } catch (IOException e) {
            err.println(file.getName() + " (" + e.getMessage() + ")");
        }
    }
