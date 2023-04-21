    public String writeBundledImage(TreeLogger logger, GeneratorContext context, int floorColor) throws UnableToCompleteException {
        BufferedImage bundledImage = drawBundledImage(logger, floorColor);
        byte[] imageBytes;
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bundledImage, BUNDLE_FILE_TYPE, byteOutputStream);
            imageBytes = byteOutputStream.toByteArray();
        } catch (IOException e) {
            logger.log(TreeLogger.ERROR, "Unable to generate file name for image bundle file", null);
            throw new UnableToCompleteException();
        }
        String bundleFileName = Util.computeStrongName(imageBytes) + ".cache." + BUNDLE_FILE_TYPE;
        OutputStream outStream = context.tryCreateResource(logger, bundleFileName);
        if (outStream != null) {
            try {
                outStream.write(imageBytes);
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
