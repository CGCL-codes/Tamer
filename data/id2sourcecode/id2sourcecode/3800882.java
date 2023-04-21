    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/jar");
        byte[] bufor = new byte[BUF_LEN];
        FileInputStream in = new FileInputStream(FILE_NAME);
        OutputStream out = response.getOutputStream();
        while (in.read(bufor) != -1) out.write(bufor);
        in.close();
        out.close();
    }
