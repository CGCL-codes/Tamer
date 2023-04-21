    public static String[] readPostscriptNames(int type, String subFont) throws Exception {
        String[] fontNames = new String[1];
        fontNames[0] = "";
        BufferedInputStream from = new BufferedInputStream(new FileInputStream(subFont));
        ByteArrayOutputStream to = new ByteArrayOutputStream();
        byte[] buffer = new byte[65535];
        int bytes_read;
        while ((bytes_read = from.read(buffer)) != -1) to.write(buffer, 0, bytes_read);
        to.close();
        from.close();
        byte[] fontData = to.toByteArray();
        if (type == TRUETYPE || type == TRUETYPE_COLLECTION) {
            TTGlyphs currentFont = new TTGlyphs();
            fontNames = currentFont.readPostScriptFontNames(fontData);
        }
        return fontNames;
    }
