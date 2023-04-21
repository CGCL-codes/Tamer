    @Nonnull
    public BufferedImage loadImage(String fileName) throws IllegalArgumentException, MissingResourceException, IOException {
        GeneralTools.notEmpty("fileName", fileName);
        URL url = getClass().getResource("images/" + fileName);
        if (url == null) {
            String msg = "Cannot locate image [" + fileName + "]!";
            if (LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, msg);
            }
            throw new MissingResourceException(msg, getClass().getName(), fileName);
        }
        ImageReader reader = ImageIO.getImageReadersBySuffix("png").next();
        BufferedImage image;
        try {
            reader.setInput(ImageIO.createImageInputStream(url.openStream()), true, true);
            image = reader.read(0);
        } catch (IOException ioE) {
            if (LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, "Failed to load image [" + fileName + "]!");
            }
            throw ioE;
        } finally {
            reader.dispose();
        }
        if (LOG.isLoggable(Level.FINER)) LOG.log(Level.FINER, "Loaded image [" + fileName + "].");
        return image;
    }
