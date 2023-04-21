    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document(new Rectangle(850, 600));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        PdfContentByte canvas = writer.getDirectContent();
        Image img = Image.getInstance(RESOURCE);
        float w = img.getScaledWidth();
        float h = img.getScaledHeight();
        canvas.ellipse(1, 1, 848, 598);
        canvas.clip();
        canvas.newPath();
        canvas.addImage(img, w, 0, 0, h, 0, -600);
        PdfTemplate t2 = writer.getDirectContent().createTemplate(850, 600);
        PdfTransparencyGroup transGroup = new PdfTransparencyGroup();
        transGroup.put(PdfName.CS, PdfName.DEVICEGRAY);
        transGroup.setIsolated(true);
        transGroup.setKnockout(false);
        t2.setGroup(transGroup);
        int gradationStep = 30;
        float[] gradationRatioList = new float[gradationStep];
        for (int i = 0; i < gradationStep; i++) {
            gradationRatioList[i] = 1 - (float) Math.sin(Math.toRadians(90.0f / gradationStep * (i + 1)));
        }
        for (int i = 1; i < gradationStep + 1; i++) {
            t2.setLineWidth(5 * (gradationStep + 1 - i));
            t2.setGrayStroke(gradationRatioList[gradationStep - i]);
            t2.ellipse(0, 0, 850, 600);
            t2.stroke();
        }
        PdfDictionary maskDict = new PdfDictionary();
        maskDict.put(PdfName.TYPE, PdfName.MASK);
        maskDict.put(PdfName.S, new PdfName("Luminosity"));
        maskDict.put(new PdfName("G"), t2.getIndirectReference());
        PdfGState gState = new PdfGState();
        gState.put(PdfName.SMASK, maskDict);
        canvas.setGState(gState);
        canvas.addTemplate(t2, 0, 0);
        document.close();
    }
