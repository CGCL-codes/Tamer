    public String writeBundledImage(TreeLogger logger, GeneratorContext context) throws UnableToCompleteException {
        int nextLeft = 0;
        int maxHeight = 0;
        for (Iterator iter = orderedImageRects.iterator(); iter.hasNext(); ) {
            ImageRect imageRect = (ImageRect) iter.next();
            imageRect.left = nextLeft;
            nextLeft += imageRect.width;
            if (imageRect.height > maxHeight) {
                maxHeight = imageRect.height;
            }
        }
        BufferedImage bundledImage = new BufferedImage(nextLeft, maxHeight, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g2d = bundledImage.createGraphics();
        for (Iterator iter = orderedImageRects.iterator(); iter.hasNext(); ) {
            ImageRect imageRect = (ImageRect) iter.next();
            g2d.drawImage(imageRect.image, imageRect.left, 0, null);
        }
        g2d.dispose();
        byte[] hash = md5.digest();
        char[] strongName = new char[2 * hash.length];
        int j = 0;
        for (int i = 0; i < hash.length; i++) {
            strongName[j++] = Util.HEX_CHARS[(hash[i] & 0xF0) >> 4];
            strongName[j++] = Util.HEX_CHARS[hash[i] & 0x0F];
        }
        String bundleFileType = "png";
        String bundleFileName = new String(strongName) + ".cache." + bundleFileType;
        OutputStream outStream = context.tryCreateResource(logger, bundleFileName);
        if (outStream != null) {
            try {
                if (!ImageIO.write(bundledImage, bundleFileType, outStream)) {
                    logger.log(TreeLogger.ERROR, "Unsupported output file type", null);
                    throw new UnableToCompleteException();
                }
                context.commitResource(logger, outStream);
            } catch (IOException e) {
                logger.log(TreeLogger.ERROR, "Failed while writing", e);
                throw new UnableToCompleteException();
            }
        } else {
            logger.log(TreeLogger.TRACE, "Generated image bundle file already exists; no need to rewrite it.", null);
        }
        return bundleFileName;
    }
