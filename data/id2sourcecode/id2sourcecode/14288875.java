    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        InputStream is = req.getInputStream();
        byte[] buf = new byte[4096];
        ByteArrayOutputStream os = new ByteArrayOutputStream(16192);
        int l = 0;
        while ((l = is.read(buf)) > 0) os.write(buf, 0, l);
        String body = os.toString("UTF-8");
        os.close();
        is.close();
        try {
            JSONObject obj = (JSONObject) JSONValue.parse(body);
            if (obj == null) {
                log.log(Level.SEVERE, "POST argument absent ou mal formé");
                resp.sendError(521);
                return;
            }
            long cmd = 0;
            String prefix = null;
            String from = null;
            String md5Pin = null;
            JSONObject book = null;
            cmd = (Long) obj.get("cmd");
            if (cmd <= 0) {
                resp.setContentType("application/json");
                resp.getWriter().println("{ \"version\": \"" + version + "\"}");
            } else {
                from = (String) obj.get("from");
                prefix = (String) obj.get("prefix");
                md5Pin = (String) obj.get("md5Pin");
                book = (JSONObject) obj.get("book");
                SecretsTransaction tr = new SecretsTransaction((int) cmd, from, prefix, md5Pin, book, obj);
                int status = tr.getStatus();
                if (status == 0) {
                    resp.setContentType("application/json");
                    resp.getWriter().print(tr.getResult());
                } else {
                    resp.sendError(520 + status);
                }
            }
        } catch (ClassCastException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            resp.sendError(501);
        } catch (ConcurrentModificationException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            resp.sendError(502);
        }
    }
