    public static BufferedImage getScreenCapture(final Rectangle bounds) throws AWTException {
        ValidateArgument.isNotNull(bounds, ValidationTemplate.NOT_NULL, "image");
        return convertToCompatibleImage(new Robot().createScreenCapture(bounds));
    }
