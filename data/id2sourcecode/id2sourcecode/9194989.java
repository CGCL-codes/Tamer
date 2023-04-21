    public void handleResourceRequest(Component c, HttpServletRequest req, HttpServletResponse resp) {
        Image img = (Image) c;
        Object source = img.getSource();
        try {
            if (source instanceof java.net.URL) {
                URLConnection co = ((URL) source).openConnection();
                if (img.getContentType() == null) resp.setContentType(co.getContentType()); else resp.setContentType(img.getContentType());
                byte[] buf = new byte[4096];
                int count;
                InputStream in = co.getInputStream();
                OutputStream out = resp.getOutputStream();
                while ((count = in.read(buf, 0, 4096)) != -1) out.write(buf, 0, count);
                in.close();
            } else if (source instanceof java.awt.Image) {
                resp.setContentType("image/gif");
                GifEncoder enc = new GifEncoder(((java.awt.Image) source), resp.getOutputStream());
                enc.encode();
            }
        } catch (Exception e) {
            try {
                resp.setContentType("text/plain");
                PrintWriter pr = new PrintWriter(new OutputStreamWriter(resp.getOutputStream()));
                e.printStackTrace(pr);
            } catch (IOException ex) {
            }
        }
    }
