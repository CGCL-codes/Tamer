    public void stream(ServletContext sc, HttpServletResponse res) throws NoRouteException, IOException {
        File file = (File) super.data;
        FileInputStream in = null;
        OutputStream out = null;
        try {
            String mimeType = sc.getMimeType(file.getAbsolutePath());
            if (mimeType == null) mimeType = super.mime;
            res.setContentType(mimeType);
            res.setContentLength((int) file.length());
            in = new FileInputStream(file);
            out = res.getOutputStream();
            byte[] buf = new byte[4096];
            int count = 0;
            while ((count = in.read(buf)) >= 0) out.write(buf, 0, count);
        } catch (FileNotFoundException e) {
            throw new NoRouteException("File not found", e);
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }
