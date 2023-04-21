    public static BufferedImage getScreenCapture(final Rectangle bounds) throws AWTException {
        ValidateExpression.isNotNull(bounds, ValidateExpression.ARGUMENT_CANNOT_BE_NULL_TEMPLATE, "image");
        return new Robot().createScreenCapture(bounds);
    }
