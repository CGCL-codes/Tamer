    public static void sendPNGResponse(HttpServletRequest request, HttpServletResponse response, File file) throws Exception {
        OutputStream out = response.getOutputStream();
        response.setContentType("image/png");
        response.setHeader("expires", "Sun, 30 March 1969 7:00:00 GMT");
        FileInputStream fin = new FileInputStream(file);
        BufferedOutputStream bout = new BufferedOutputStream(out);
        for (int t = 0; t < file.length(); t++) {
            bout.write(fin.read());
        }
        bout.flush();
        bout.close();
        ;
    }
