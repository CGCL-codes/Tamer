    public void setVisibleSignature(String fieldName) {
        AcroFields af = writer.getAcroFields();
        AcroFields.Item item = af.getFieldItem(fieldName);
        if (item == null) throw new IllegalArgumentException("The field " + fieldName + " does not exist.");
        PdfDictionary merged = item.getMerged(0);
        if (!PdfName.SIG.equals(PdfReader.getPdfObject(merged.get(PdfName.FT)))) throw new IllegalArgumentException("The field " + fieldName + " is not a signature field.");
        this.fieldName = fieldName;
        PdfArray r = merged.getAsArray(PdfName.RECT);
        float llx = r.getAsNumber(0).floatValue();
        float lly = r.getAsNumber(1).floatValue();
        float urx = r.getAsNumber(2).floatValue();
        float ury = r.getAsNumber(3).floatValue();
        pageRect = new Rectangle(llx, lly, urx, ury);
        pageRect.normalize();
        page = item.getPage(0).intValue();
        int rotation = writer.reader.getPageRotation(page);
        Rectangle pageSize = writer.reader.getPageSizeWithRotation(page);
        switch(rotation) {
            case 90:
                pageRect = new Rectangle(pageRect.getBottom(), pageSize.getTop() - pageRect.getLeft(), pageRect.getTop(), pageSize.getTop() - pageRect.getRight());
                break;
            case 180:
                pageRect = new Rectangle(pageSize.getRight() - pageRect.getLeft(), pageSize.getTop() - pageRect.getBottom(), pageSize.getRight() - pageRect.getRight(), pageSize.getTop() - pageRect.getTop());
                break;
            case 270:
                pageRect = new Rectangle(pageSize.getRight() - pageRect.getBottom(), pageRect.getLeft(), pageSize.getRight() - pageRect.getTop(), pageRect.getRight());
                break;
        }
        if (rotation != 0) pageRect.normalize();
        rect = new Rectangle(this.pageRect.getWidth(), this.pageRect.getHeight());
    }
