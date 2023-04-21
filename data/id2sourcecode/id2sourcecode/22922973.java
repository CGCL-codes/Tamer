    protected void doBrowse(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String browseUrl = req.getParameter(PARAM_URL);
        if (!browseUrl.matches(fAllow)) {
            printError(req, resp, "Prohibited URL!", null);
            return;
        }
        try {
            URL url = new URL(browseUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            String authHeader = req.getHeader("Authorization");
            if (authHeader != null) {
                conn.setRequestProperty("Authorization", authHeader);
            }
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                resp.setHeader("WWW-Authenticate", conn.getHeaderField("WWW-Authenticate"));
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization Required");
                return;
            }
            Source stylesheet = getStylesheet(conn.getContentType());
            OutputStream out = null;
            InputStream in = new BufferedInputStream(conn.getInputStream(), BUFFER_SIZE);
            if (stylesheet == null) {
                resp.setContentType(conn.getContentType());
                out = new BufferedOutputStream(resp.getOutputStream(), BUFFER_SIZE);
                byte[] buffer = new byte[BUFFER_SIZE];
                int b;
                while ((b = in.read(buffer)) > -1) {
                    out.write(buffer, 0, b);
                }
            } else {
                TransformerFactory f = TransformerFactory.newInstance();
                Transformer t = f.newTransformer(stylesheet);
                t.setParameter("browseUrl", InfoUtil.getServletUrl(req) + "?url=");
                t.setParameter("auxRoot", InfoUtil.getAuxRoot(req, fAuxRoot));
                resp.setContentType("text/html");
                out = new BufferedOutputStream(resp.getOutputStream(), BUFFER_SIZE);
                Source s = new StreamSource(in);
                Result r = new StreamResult(out);
                t.transform(s, r);
            }
            try {
                out.flush();
                out.close();
            } catch (Exception e) {
            }
            try {
                in.close();
            } catch (Exception e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            printError(req, resp, e.getMessage(), e);
            return;
        }
    }
