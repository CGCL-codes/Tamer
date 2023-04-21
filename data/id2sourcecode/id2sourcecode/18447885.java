    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        String uri = request.getRequestURI();
        String mt = context.getMimeType(uri);
        if (mt == null) response.setContentType("text/html"); else response.setContentType(mt);
        if (!File.separator.equals("/")) {
        }
        int defcnt = 0;
        boolean tryDefault = false;
        if (defFiles.length > 0 && uri.endsWith("/")) tryDefault = true;
        File rootFile = new File(docRoot, uri);
        File sendFile;
        if (tryDefault) sendFile = new File(rootFile, defFiles[defcnt++]); else sendFile = rootFile;
        FileInputStream in;
        while (true) {
            try {
                in = new FileInputStream(sendFile);
                break;
            } catch (java.io.FileNotFoundException e) {
                if (defcnt > defFiles.length || !tryDefault) {
                    response.sendError(404, e.toString());
                    return;
                }
                sendFile = new File(rootFile, defFiles[defcnt++]);
            }
        }
        response.setContentLength((int) sendFile.length());
        java.io.OutputStream out = response.getOutputStream();
        byte[] b = new byte[1024];
        int cnt;
        while ((cnt = in.read(b, 0, b.length)) > -1) out.write(b, 0, cnt);
        in.close();
        out.close();
    }
