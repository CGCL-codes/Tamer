    public Map createFont(Map values, String fontID, boolean renderPage, Map descFontValues, ObjectStore objectStore) throws Exception {
        LogWriter.writeMethod("{readTrueTypeFont}" + values, 0);
        fontTypes = StandardFonts.TRUETYPE;
        Map fontDescriptor = super.createFont(values, fontID, renderPage, descFontValues, objectStore);
        if (renderPage) {
            if ((fontDescriptor != null)) {
                Object fontFileRef = fontDescriptor.get("FontFile2");
                try {
                    if (fontFileRef != null) {
                        byte[] stream;
                        if (fontFileRef instanceof String) stream = currentPdfFile.readStream((String) fontFileRef, true); else stream = (byte[]) ((Map) fontFileRef).get("DecodedStream");
                        readEmbeddedFont(stream, hasEncoding, false);
                    }
                } catch (Exception e) {
                }
            }
            if ((!isFontEmbedded) && (substituteFont != null)) {
                if (glyphs.remapFont) glyphs.remapFont = false;
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
        }
        readWidths(values);
        if (renderPage) setFont(glyphs.fontName, 1);
        return fontDescriptor;
    }
