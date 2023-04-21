    public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
        float middle = (llx + urx) / 2;
        canvas.beginText();
        canvas.setFontAndSize(FONT, 10);
        canvas.showTextAligned(Element.ALIGN_CENTER, "*", middle, y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, "*  *", middle, y - 10, 0);
        canvas.endText();
    }
