    public void handle(Request request, Response response) {
        response.setCode(200);
        response.setDate("Date", System.currentTimeMillis());
        response.setContentLength(request.getContentLength());
        byte[] buffer = new byte[4096];
        int written = 0;
        int length = request.getContentLength();
        try {
            InputStream in = request.getInputStream();
            OutputStream out = response.getOutputStream(4096);
            int read;
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
                written += read;
            }
            in.close();
            out.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (written != length) {
                System.out.println(written + " " + length);
            }
        }
    }
