    public PDFStream getFontFile(int i) {
        InputStream instream = null;
        if (embedFileName != null) try {
            instream = embedFileName.openStream();
        } catch (Exception e) {
            MessageHandler.error("Failed to embed fontfile: " + embedFileName);
        }
        if (instream == null && embedResourceName != null) try {
            instream = new BufferedInputStream(this.getClass().getResourceAsStream(embedResourceName));
        } catch (Exception e) {
            MessageHandler.error("Failed to embed fontresource: " + embedResourceName);
        }
        if (instream == null) {
            return (PDFStream) null;
        }
        try {
            if (subType == org.apache.fop.pdf.PDFFont.TYPE1) {
                PFBParser parser = new PFBParser();
                PFBData pfb = parser.parsePFB(instream);
                embeddedFont = new PDFT1Stream(i);
                ((PDFT1Stream) embeddedFont).setData(pfb);
            } else {
                byte[] file = IOUtil.toByteArray(instream, 128000);
                embeddedFont = new PDFTTFStream(i, file.length);
                ((PDFTTFStream) embeddedFont).setData(file, file.length);
            }
            embeddedFont.addFilter("flate");
            embeddedFont.addFilter("ascii-85");
            instream.close();
        } catch (Exception e) {
            MessageHandler.error("Failed to read font data for embedded font: " + e.getMessage());
        }
        return (PDFStream) embeddedFont;
    }
