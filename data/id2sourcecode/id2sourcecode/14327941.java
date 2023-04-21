    @Override
    public void scale(File src, File dest, int w, int h) throws ImgException {
        if (!dest.getParentFile().exists()) dest.getParentFile().mkdirs();
        try {
            if (w <= 0 && h <= 0) {
                FileUtils.copyFile(src, dest);
                return;
            }
            String ext = FilenameUtils.getExtension(dest.getName()).toLowerCase();
            if ("gif".equalsIgnoreCase(ext)) {
                GifImage gifImage = GifDecoder.decode(src);
                GifImage newGif = GifTransformer.resize(gifImage, w, h, false);
                GifEncoder.encode(newGif, dest);
            } else {
                BufferedImage bi = (BufferedImage) ImageIO.read(src);
                MyScaleFilter scale = new MyScaleFilter(w, h);
                BufferedImage bi_scale = new BufferedImage(w, h, (bi.getType() != 0) ? bi.getType() : BufferedImage.TYPE_INT_RGB);
                scale.filter(bi, bi_scale);
                ImageIO.write(bi_scale, ext.equals("png") ? "png" : "jpeg", dest);
            }
        } catch (IOException e) {
            throw new ImgException("Exception occur when scaling image.", e);
        }
    }
