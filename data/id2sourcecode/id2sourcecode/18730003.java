    protected void showTextFile(String name, String params, File f, int length, HttpRequest req, HttpResponse res, boolean show) throws IOException {
        byte[] buf;
        OutputStream out;
        int c;
        if (Util.logLevel > 4) Util.logDebug("web server: show text file:" + name);
        res.addHeader("Last-Modified", Util.formatDateTime(f.lastModified()));
        if (show) res.addHeader("Content-Type", "text/plain");
        res.setContentLength(length);
        InputStream in = new FileInputStream(f);
        buf = new byte[Util.BUF_SIZE];
        out = res.getOutputStream();
        while ((c = in.read(buf)) != -1) out.write(buf, 0, c);
        in.close();
    }
