    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        OutputStream os = response.getOutputStream();
        InputStream is = request.getInputStream();
        byte[] b = new byte[256];
        int read;
        while ((read = is.read(b)) != -1) {
            os.write(b, 0, read);
        }
    }
