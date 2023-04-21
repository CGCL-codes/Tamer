    private void copyStream(GrizzlyResponse grizzlyResponse, InputStream is) throws IOException {
        OutputStream os = grizzlyResponse.getOutputStream();
        byte[] buffer = new byte[8096];
        int read = 0;
        while ((read = is.read(buffer)) > 0) {
            os.write(buffer, 0, read);
        }
        os.flush();
        is.close();
    }
