    public void setVisibleSignature(Rectangle pageRect, int page, String fieldName) {
        if (fieldName != null) {
            if (fieldName.indexOf('.') >= 0) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("field.names.cannot.contain.a.dot"));
            AcroFields af = writer.getAcroFields();
            AcroFields.Item item = af.getFieldItem(fieldName);
            if (item != null) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.field.1.already.exists", fieldName));
            this.fieldName = fieldName;
        }
        if (page < 1 || page > writer.reader.getNumberOfPages()) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.page.number.1", page));
        this.pageRect = new Rectangle(pageRect);
        this.pageRect.normalize();
        rect = new Rectangle(this.pageRect.getWidth(), this.pageRect.getHeight());
        this.page = page;
        newField = true;
    }
