    public Color getShade(final Color lightColor, final Matrix lightSource) {
        final Matrix normal = getNormal(getCoordinates());
        if (normal == null) {
            return boundsColor();
        }
        double cosang = lightSource.dot(normal) / (MatrixUtil.normF(lightSource) * MatrixUtil.normF(normal));
        if (normal.get(1, 0) < 0) {
            cosang = -cosang;
        }
        final double intensity = (cosang + 1) / 2;
        final double red = lightColor.getRed() * intensity;
        final double green = lightColor.getGreen() * intensity;
        final double blue = lightColor.getBlue() * intensity;
        final int iRed = (int) red;
        final int iGreen = (int) green;
        final int iBlue = (int) blue;
        return new Color(Math.max(Math.min(iRed, 255), 0), Math.max(Math.min(iGreen, 255), 0), Math.max(Math.min(iBlue, 255), 0), lightColor.getAlpha());
    }
