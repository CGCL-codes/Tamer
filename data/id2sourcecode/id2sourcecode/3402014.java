    protected void initImage(URL _url) {
        String u = "" + _url;
        setDescription(u);
        Image img = null;
        if ((u.endsWith(".bmp") || u.endsWith(".BMP"))) {
            try {
                BuBmpLoader l = new BuBmpLoader();
                l.read(_url.openStream());
                img = BuLib.HELPER.getToolkit().createImage(l.getImageSource());
            } catch (Exception ex) {
            }
        }
        if (img == null) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = _url.openStream();
                FuLib.copyFully(in, out);
                in.close();
                img = BuLib.HELPER.getToolkit().createImage(out.toByteArray());
            } catch (Exception ex) {
            }
        }
        setImage(img);
    }
