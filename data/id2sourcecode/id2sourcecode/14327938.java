    public int[] shrink(File src, File dest, int size) throws ImgException {
        try {
            BufferedImage orig_portrait = (BufferedImage) ImageIO.read(src);
            int w = orig_portrait.getWidth();
            int h = orig_portrait.getHeight();
            if (w <= size && h <= size) {
                FileUtils.copyFile(src, dest);
                return new int[] { w, h };
            } else {
                double ratio = (w > h) ? (double) size / w : (double) size / h;
                int w2 = (int) (w * ratio);
                int h2 = (int) (h * ratio);
                scale(src, dest, w2, h2);
                return new int[] { w2, h2 };
            }
        } catch (IOException e) {
            throw new ImgException("Exception occur when shrink image.", e);
        }
    }
