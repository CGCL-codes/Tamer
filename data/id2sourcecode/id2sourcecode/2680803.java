    protected void writeAlifeImage(ZipOutputStream output, PropertyFile spriteCfg) throws IOException {
        if (!(alifeImage instanceof AlifeBackground)) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        AlifeBackground background = (AlifeBackground) alifeImage;
        spriteCfg.setProperty(PROPERTY_SECTION_SPRITE, PROPERTY_SPEED).setValue(background.getSpeed());
        spriteCfg.setProperty(PROPERTY_SECTION_SPRITE, PROPERTY_FOREGROUND).setValue(background.isForeground());
        output.putNextEntry(new ZipEntry(background.getName() + "." + IMAGE_FORMAT));
        ImageIO.write((RenderedImage) background.getImage(), IMAGE_FORMAT, output);
    }
