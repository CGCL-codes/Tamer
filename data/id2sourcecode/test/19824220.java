    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ServletOutputStream sos = res.getOutputStream();
        String qString = req.getQueryString();
        String reqUri = req.getRequestURI();
        if (servletPath == null) {
            int nextSlash = reqUri.indexOf('/', 1);
            if (nextSlash == -1) servletPath = reqUri; else servletPath = reqUri.substring(0, nextSlash);
        }
        String destUrl = destination + reqUri.substring(servletPath.length()) + (qString == null ? "" : "?" + qString);
        HttpURLConnection connection = (HttpURLConnection) new java.net.URL(destUrl).openConnection();
        connection.setRequestMethod(req.getMethod());
        copyHeaders(req, connection);
        connection.connect();
        try {
            int status = connection.getResponseCode();
            res.setStatus(status);
            if (debug) {
                System.err.println(req.getRequestURL().toString() + "\n\t-> " + destUrl + "\n\t = " + connection.getResponseCode());
                System.err.println("---  Response ---");
            }
            String key, value;
            int c = 0;
            while ((value = connection.getHeaderField(c)) != null) {
                if ((key = connection.getHeaderFieldKey(c++)) != null) {
                    res.setHeader(key, value);
                }
                if (debug) System.err.println(key + ": " + connection.getHeaderField(c - 1));
            }
            if (status >= 200 && status < 300) {
                byte[] buff = new byte[1024];
                int nRead;
                java.io.InputStream is = connection.getInputStream();
                while ((nRead = is.read(buff)) != -1) sos.write(buff, 0, nRead);
            }
        } finally {
            connection.disconnect();
        }
    }
