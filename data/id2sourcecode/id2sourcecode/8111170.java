    public void createFont(String fontName) throws Exception {
        fontTypes = StandardFonts.TRUETYPE;
        setBaseFontName(fontName);
        BufferedInputStream from;
        InputStream jarFile = loader.getResourceAsStream(substituteFont);
        if (jarFile == null) from = new BufferedInputStream(new FileInputStream(substituteFont)); else from = new BufferedInputStream(jarFile);
        ByteArrayOutputStream to = new ByteArrayOutputStream();
        byte[] buffer = new byte[65535];
        int bytes_read;
        while ((bytes_read = from.read(buffer)) != -1) to.write(buffer, 0, bytes_read);
        to.close();
        from.close();
        readEmbeddedFont(to.toByteArray(), false, true);
        isFontSubstituted = true;
    }
