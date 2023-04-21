    public void service(HttpServletRequest req, HttpServletResponse res) {
        String targetUrl = req.getParameter("url");
        String cookie = req.getParameter("cookie");
        if (targetUrl != null) {
            try {
                targetUrl = ObjectDeserializer.deserializeString(Base64.decode(targetUrl));
                URL url = new URL(targetUrl);
                URLConnection con = url.openConnection();
                cookie = ObjectDeserializer.deserializeString(Base64.decode(cookie));
                con.setRequestProperty("Cookie", cookie);
                con.connect();
                res.setContentType(con.getContentType());
                res.setContentLength(con.getContentLength());
                InputStream in = con.getInputStream();
                OutputStream out = res.getOutputStream();
                int next;
                while ((next = in.read()) != -1) {
                    out.write(next);
                }
                out.flush();
                out.close();
            } catch (Exception e) {
                _log.warn(e);
            }
        }
    }
