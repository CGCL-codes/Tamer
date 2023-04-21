    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        while (true) {
            if (in == null) {
                try {
                    HttpRequest request = httpClient.createRequest(HttpRequest.Method.POST, "/connections/" + connectionId);
                    HttpResponse response = request.execute();
                    in = response.getInputStream();
                } catch (HttpException ex) {
                    throw new VCHConnectionException(ex);
                }
            }
            int c = in.read(b, off, len);
            if (c == -1) {
                in = null;
            } else {
                return c;
            }
        }
    }
