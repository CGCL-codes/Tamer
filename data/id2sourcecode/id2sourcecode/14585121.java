    private byte[] readHttpRequestBody(HttpServletRequest req) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream reqIS = req.getInputStream();
        int readed;
        while ((readed = reqIS.read()) != -1) {
            baos.write(readed);
        }
        baos.close();
        byte[] requestBody = baos.toByteArray();
        return requestBody;
    }
