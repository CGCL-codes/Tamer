    private String bufferFileData() throws IOException {
        final URL url = new URL(inputReference);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return VORGURLRequest.getResourceData(conn);
    }
