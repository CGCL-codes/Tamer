    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String request = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int read = 0;
        InputStream res = req.getInputStream();
        while (true) {
            try {
                read = res.read(buf);
            } catch (IOException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                res.close();
                return;
            }
            if (read == -1) {
                break;
            }
            baos.write(buf, 0, read);
            if (baos.size() > maxPostLimit()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Data size reaches the limit of Java2Script Simple RPC!");
                res.close();
                return;
            }
        }
        request = baos.toString();
        res.close();
        SimpleRPCRunnable runnable = getRunnableByRequest(request);
        if (runnable == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("text/plain; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        SimpleRPCRunnable clonedRunnable = null;
        try {
            clonedRunnable = (SimpleRPCRunnable) runnable.clone();
        } catch (CloneNotSupportedException e) {
        }
        runnable.ajaxRun();
        final String[] diffs = SimpleRPCUtils.compareDiffs(runnable, clonedRunnable);
        String serialize = runnable.serialize(new SimpleFilter() {

            public boolean accept(String field) {
                for (int i = 0; i < diffs.length; i++) {
                    if (diffs[i].equals(field)) {
                        return true;
                    }
                }
                return false;
            }

            public boolean ignoreDefaultFields() {
                return false;
            }
        });
        writer.write(serialize);
        runnable.ajaxOut();
    }
