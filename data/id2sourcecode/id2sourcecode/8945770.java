    public static void grayFilter(String image) {
        ImageOutputStream output = null;
        try {
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            output = new FileImageOutputStream(new File(image));
            ImageIO.write(op.filter(ImageIO.read(new File(image)), null), "jpg", output);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                if (output != null) {
                    output = null;
                }
            }
        }
    }
