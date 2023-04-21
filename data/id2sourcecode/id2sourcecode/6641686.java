    public boolean uploadFile(URL url, File tmpName, String fileName, String extension, String description) throws IOException {
        String charset = "UTF-8";
        String parameters = extension + "&" + description;
        File zipFile = new File(fileName);
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.addRequestProperty("Cookie", session);
        PrintWriter writer = null;
        try {
            OutputStream output = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"parameters\"; prms=\"" + parameters + "\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF);
            writer.append(parameters);
            writer.append(CRLF);
            writer.flush();
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + zipFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(zipFile.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            InputStream input = null;
            try {
                input = new FileInputStream(tmpName);
                byte[] buffer = new byte[1024];
                for (int length = 0; (length = input.read(buffer)) > 0; ) {
                    output.write(buffer, 0, length);
                }
                output.flush();
            } finally {
                if (input != null) try {
                    input.close();
                } catch (IOException logOrIgnore) {
                }
            }
            writer.append(CRLF).flush();
            writer.append("--" + boundary + "--").append(CRLF);
        } finally {
            if (writer != null) writer.close();
        }
        POConfirmation confirm = null;
        confirm = (POConfirmation) inputObject(connection);
        return confirm.isSuccessful();
    }
