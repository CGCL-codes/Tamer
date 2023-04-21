    private boolean streamFile(HttpServletRequest req, HttpServletResponse res, File source) throws IOException {
        if (!source.exists() || !source.canRead()) {
            VPHelpers.error(req, res, "Cannot read file \"" + source.getPath() + "\".");
        }
        try {
            OutputStream out = res.getOutputStream();
            InputStream in = new FileInputStream(source);
            while (in.available() > 0) out.write(in.read());
            in.close();
            out.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
