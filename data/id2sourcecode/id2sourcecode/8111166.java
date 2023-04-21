    protected void substituteFontUsed(String substituteFontFile, String substituteFontName) throws PdfFontException {
        InputStream from = null;
        try {
            from = loader.getResourceAsStream("org/jpedal/res/fonts/" + substituteFontFile);
        } catch (Exception e) {
            System.err.println("Exception " + e + " reading " + substituteFontFile + " Check cid  jar installed");
            LogWriter.writeLog("Exception " + e + " reading " + substituteFontFile + " Check cid  jar installed");
        }
        if (from == null) throw new PdfFontException("Unable to load font " + substituteFontFile);
        try {
            ByteArrayOutputStream to = new ByteArrayOutputStream();
            byte[] buffer = new byte[65535];
            int bytes_read;
            while ((bytes_read = from.read(buffer)) != -1) to.write(buffer, 0, bytes_read);
            to.close();
            from.close();
            readFontData(to.toByteArray());
            glyphs.setEncodingToUse(hasEncoding, this.getFontEncoding(false), true, isCIDFont);
            isFontEmbedded = true;
        } catch (Exception e) {
            System.err.println("Exception " + e + " reading " + substituteFontFile + " Check cid  jar installed");
            LogWriter.writeLog("Exception " + e + " reading " + substituteFontFile + " Check cid  jar installed");
        }
    }
