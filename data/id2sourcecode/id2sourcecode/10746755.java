    public BufferedImage cutToSquare(BufferedImage srcBi, double size) throws GraphException {
        if (size <= 0) throw new SizeArgumentException("size");
        int srcBiWidth = srcBi.getWidth();
        int srcBiHeight = srcBi.getHeight();
        if (srcBiWidth == srcBiHeight) return resize(srcBi, size, size);
        BufferedImage destBi = null;
        if (srcBiWidth > srcBiHeight) {
            int begin = (srcBiWidth - srcBiHeight) / 2;
            int end = (srcBiWidth + srcBiHeight) / 2;
            destBi = new BufferedImage(srcBiHeight, srcBiHeight, srcBi.getType());
            Graphics2D newGraphics = (Graphics2D) destBi.getGraphics();
            newGraphics.setRenderingHints(renderHints);
            newGraphics.drawImage(srcBi, 0, 0, srcBiHeight, srcBiHeight, begin, 0, end, srcBiHeight, null);
        } else {
            int begin = (srcBiHeight - srcBiWidth) / 2;
            int end = (srcBiWidth + srcBiHeight) / 2;
            destBi = new BufferedImage(srcBiWidth, srcBiWidth, srcBi.getType());
            Graphics2D newGraphics = (Graphics2D) destBi.getGraphics();
            newGraphics.setRenderingHints(renderHints);
            newGraphics.drawImage(srcBi, 0, 0, srcBiWidth, srcBiWidth, 0, begin, srcBiWidth, end, null);
        }
        return resize(destBi, size, size);
    }
