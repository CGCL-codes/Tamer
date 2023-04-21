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
            String md5Pin = null;
            String book = null;
            cmd = (Long) obj.get("cmd");
            prefix = (String) obj.get("prefix");
            md5Pin = (String) obj.get("md5Pin");
            book = (String) obj.get("book");
            AdminTransaction tr = new AdminTransaction((int) cmd, prefix, md5Pin, book);
            int status = tr.getStatus();
            if (status == 0) {
                resp.setContentType("application/json");
                resp.getWriter().print(tr.getResult());
            } else {
                resp.sendError(520 + status);
            }
        } catch (ClassCastException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            resp.sendError(501);
        } catch (ConcurrentModificationException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            resp.sendError(502);
        }
    }
