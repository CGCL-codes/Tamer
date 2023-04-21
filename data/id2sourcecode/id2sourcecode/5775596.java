    private void appendemptypageatend(PdfReader reader, PdfWriter writer) throws IOException {
        System.out.println("last page odd. add page!");
        PdfDictionary page = reader.getPageN(reader.getNumberOfPages());
        PdfDictionary parent = page.getAsDict(PdfName.PARENT);
        PdfArray kids = parent.getAsArray(PdfName.KIDS);
        PdfIndirectReference ref = writer.getPdfIndirectReference();
        kids.add(ref);
        PdfDictionary newPage = new PdfDictionary(PdfName.PAGE);
        newPage.merge(lastpage);
        newPage.remove(PdfName.CONTENTS);
        newPage.remove(PdfName.ANNOTS);
        newPage.put(PdfName.RESOURCES, new PdfDictionary());
        writer.addToBody(newPage, ref);
        PdfNumber count = null;
        while (parent != null) {
            count = parent.getAsNumber(PdfName.COUNT);
            parent.put(PdfName.COUNT, new PdfNumber(count.intValue() + 1));
            parent = parent.getAsDict(PdfName.PARENT);
        }
    }
