    public HttpResponse getResponseOLD(URL url) {
        HttpResponse response = null;
        int bytesRead = 0;
        int bytesToRead = 1024;
        byte[] input = new byte[bytesToRead];
        InputStream in;
        try {
            in = url.openStream();
            while (bytesRead < bytesToRead) {
                int result = in.read(input, bytesRead, bytesToRead - bytesRead);
                if (result == -1) break;
                bytesRead += result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        response = new HttpResponse();
        response.setContent(input);
        return response;
    }
