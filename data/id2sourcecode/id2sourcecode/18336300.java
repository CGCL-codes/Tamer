    protected void readEmbeddedFont(Map values, Map fontDescriptor) throws Exception {
        if (substituteFont != null) {
            BufferedInputStream from = new BufferedInputStream(loader.getResourceAsStream(substituteFont));
            ByteArrayOutputStream to = new ByteArrayOutputStream();
            byte[] buffer = new byte[65535];
            int bytes_read;
            while ((bytes_read = from.read(buffer)) != -1) to.write(buffer, 0, bytes_read);
            to.close();
            from.close();
            try {
                if (substituteFont.indexOf("/t1/") != -1) readType1FontFile(to.toByteArray()); else readType1CFontFile(to.toByteArray());
            } catch (Exception e) {
                LogWriter.writeLog("[PDF]Substitute font=" + substituteFont + "Type 1 exception=" + e);
            }
        } else {
            String fontFileRef = (String) fontDescriptor.get("FontFile");
            if (fontFileRef != null) {
                try {
                    readType1FontFile(currentPdfFile.readStream(fontFileRef, true));
                } catch (Exception e) {
                    System.out.println("Type 1 exception=" + e);
                }
            } else {
                Object objectFontFileRef = fontDescriptor.get("FontFile3");
                try {
                    if (objectFontFileRef != null) {
                        byte[] stream;
                        if (objectFontFileRef instanceof String) stream = currentPdfFile.readStream((String) objectFontFileRef, true); else stream = (byte[]) ((Map) objectFontFileRef).get("DecodedStream");
                        if (stream != null) readType1CFontFile(stream);
                    }
                } catch (Exception e) {
                }
            }
        }
    }
