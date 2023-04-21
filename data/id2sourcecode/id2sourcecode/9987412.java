    protected void service(HttpServletRequest aRequest, HttpServletResponse aResponse) throws ServletException, IOException {
        String uri = aRequest.getRequestURI();
        if ("/".equals(uri)) uri = "/JBossInstaller.html";
        StringTokenizer st = new StringTokenizer(uri, "/");
        String last = "no";
        while (st.hasMoreTokens()) {
            last = st.nextToken();
        }
        InputStream in = getClass().getResourceAsStream("/" + last);
        byte[] buf = new byte[1024];
        int readed;
        OutputStream out = aResponse.getOutputStream();
        while ((readed = in.read(buf, 0, 1024)) > 0) {
            out.write(buf, 0, readed);
        }
        in.close();
        out.flush();
    }
