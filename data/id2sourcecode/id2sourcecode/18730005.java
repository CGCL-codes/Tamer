    protected void handleDoGet(HttpRequest req, HttpResponse res) throws IOException {
        byte[] buf;
        OutputStream out;
        int c;
        String name = req.getRequestURI();
        if (name == null) {
            super.doGet(req, res);
            return;
        }
        if (!name.startsWith("/JavaBridge")) {
            if (name.startsWith("/")) name = name.substring(1);
            String params = null;
            int idx = name.indexOf('?');
            if (idx != -1) {
                params = name.substring(idx + 1);
                name = name.substring(0, idx);
            }
            File f = Standalone.getCanonicalWindowsFile(name);
            if (f == null || !f.exists()) f = new File(Util.HOME_DIR, name);
            if (f == null || !f.exists()) return;
            if (f.isHidden()) return;
            long l = f.length();
            if (l >= Integer.MAX_VALUE) throw new IOException("file " + name + " too large");
            int length = (int) l;
            if (showDirectory(name, f, length, req, res)) return;
            if (handleScriptContent(name, params, f, length, req, res)) return;
            showTextFile(name, params, f, length, req, res, (!name.endsWith(".html")) || "show".equals(params));
            return;
        }
        if (cache != null && name.endsWith("Java.inc")) {
            res.setContentLength(cache.length);
            res.getOutputStream().write(cache);
            return;
        }
        if (Util.JAVA_INC != null && name.endsWith("Java.inc")) {
            try {
                Field f = Util.JAVA_INC.getField("bytes");
                cache = buf = (byte[]) f.get(Util.JAVA_INC);
                res.setContentLength(buf.length);
                out = res.getOutputStream();
                out.write(buf);
                return;
            } catch (SecurityException e) {
            } catch (Exception e) {
                Util.printStackTrace(e);
            }
        }
        name = name.replaceFirst("/JavaBridge", "META-INF");
        InputStream in = JavaBridgeRunner.class.getClassLoader().getResourceAsStream(name);
        if (in == null) {
            name = name.replaceFirst("Java\\.inc", "JavaBridge.inc");
            in = JavaBridgeRunner.class.getClassLoader().getResourceAsStream(name);
            if (in == null) {
                res.setContentLength(ERROR.length);
                res.getOutputStream().write(ERROR);
                return;
            } else {
                if (Util.logLevel > 4) Util.logDebug("Java.inc not found, using JavaBridge.inc instead");
            }
        }
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            buf = new byte[Util.BUF_SIZE];
            while ((c = in.read(buf)) > 0) bout.write(buf, 0, c);
            res.addHeader("Last-Modified", "Wed, 17 Jan 2007 19:52:43 GMT");
            res.setContentLength(bout.size());
            out = res.getOutputStream();
            out.write(bout.toByteArray());
        } catch (IOException e) {
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }
