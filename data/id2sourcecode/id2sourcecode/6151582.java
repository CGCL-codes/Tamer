    public void setVisibleSignature(String fieldName) {
        AcroFields af = writer.getAcroFields();
        AcroFields.Item item = af.getFieldItem(fieldName);
        if (item == null) throw new IllegalArgumentException("The field " + fieldName + " does not exist.");
        PdfDictionary merged = (PdfDictionary) item.merged.get(0);
        if (!PdfName.SIG.equals(PdfReader.getPdfObject(merged.get(PdfName.FT)))) throw new IllegalArgumentException("The field " + fieldName + " is not a signature field.");
        this.fieldName = fieldName;
        PdfArray r = (PdfArray) PdfReader.getPdfObject(merged.get(PdfName.RECT));
        ArrayList ar = r.getArrayList();
        float llx = ((PdfNumber) PdfReader.getPdfObject((PdfObject) ar.get(0))).floatValue();
        float lly = ((PdfNumber) PdfReader.getPdfObject((PdfObject) ar.get(1))).floatValue();
        float urx = ((PdfNumber) PdfReader.getPdfObject((PdfObject) ar.get(2))).floatValue();
        float ury = ((PdfNumber) PdfReader.getPdfObject((PdfObject) ar.get(3))).floatValue();
        pageRect = new Rectangle(llx, lly, urx, ury);
        pageRect.normalize();
        page = ((Integer) item.page.get(0)).intValue();
        int rotation = writer.reader.getPageRotation(page);
        Rectangle pageSize = writer.reader.getPageSizeWithRotation(page);
        switch(rotation) {
            case 90:
                pageRect = new Rectangle(pageRect.bottom(), pageSize.top() - pageRect.left(), pageRect.top(), pageSize.top() - pageRect.right());
                break;
            case 180:
                pageRect = new Rectangle(pageSize.right() - pageRect.left(), pageSize.top() - pageRect.bottom(), pageSize.right() - pageRect.right(), pageSize.top() - pageRect.top());
                break;
            case 270:
                pageRect = new Rectangle(pageSize.right() - pageRect.bottom(), pageRect.left(), pageSize.right() - pageRect.top(), pageRect.right());
                break;
        }
        if (rotation != 0) pageRect.normalize();
        rect = new Rectangle(this.pageRect.width(), this.pageRect.height());
    }
