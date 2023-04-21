    public ImgJBIG2(int width, int height, byte[] data, byte[] globals) {
        super((URL) null);
        type = JBIG2;
        originalType = ORIGINAL_JBIG2;
        scaledHeight = height;
        setTop(scaledHeight);
        scaledWidth = width;
        setRight(scaledWidth);
        bpc = 1;
        colorspace = 1;
        rawData = data;
        plainWidth = getWidth();
        plainHeight = getHeight();
        if (globals != null) {
            this.global = globals;
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(this.global);
                this.globalHash = md.digest();
            } catch (Exception e) {
            }
        }
    }
