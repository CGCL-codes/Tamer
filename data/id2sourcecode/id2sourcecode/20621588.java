    public void openItem() throws DocumentHandlerException {
        super.openItem();
        if (!isValid()) {
            return;
        }
        this.source = evalAttribute(CommonAttributes.IMAGE_SOURCE);
        this.page = getIntegerAttribute(CommonAttributes.IMAGE_PAGE, 1, true, null);
        String rotateAttr = getStringAttribute(CommonAttributes.IMAGE_ROTATE, true, CommonStyleAttributes.IMAGE_ROTATE);
        boolean rotate = false;
        if (rotateAttr != null) {
            rotate = new Boolean(rotateAttr).booleanValue();
        }
        byte[] content = null;
        try {
            if (source instanceof byte[]) {
                content = (byte[]) source;
            } else if (source instanceof File) {
                content = FileUtils.loadFile((File) source);
            } else {
                content = documentHandler.loadResource(source.toString());
            }
            PdfWriter writer = (PdfWriter) documentHandler.getDocumentWriter();
            PdfReader reader = new PdfReader(content);
            if (page < 0) {
                throw new DocumentHandlerException(locator(), "The page number must be 1 at least!");
            }
            PdfImportedPage importedPage = writer.getImportedPage(reader, page);
            Rectangle bbox = importedPage.getBoundingBox();
            Rectangle oldBox = new Rectangle(bbox);
            if (isAttributeDefined(CommonAttributes.MARGIN_LEFT, CommonStyleAttributes.MARGIN_LEFT)) {
                float margin = getDimensionAttribute(CommonAttributes.MARGIN_LEFT, true, CommonStyleAttributes.MARGIN_LEFT);
                bbox.setLeft(margin);
            }
            if (isAttributeDefined(CommonAttributes.MARGIN_RIGHT, CommonStyleAttributes.MARGIN_RIGHT)) {
                float margin = getDimensionAttribute(CommonAttributes.MARGIN_RIGHT, true, CommonStyleAttributes.MARGIN_RIGHT);
                bbox.setRight(oldBox.getRight() - margin);
            }
            if (isAttributeDefined(CommonAttributes.MARGIN_BOTTOM, CommonStyleAttributes.MARGIN_BOTTOM)) {
                float margin = getDimensionAttribute(CommonAttributes.MARGIN_BOTTOM, true, CommonStyleAttributes.MARGIN_BOTTOM);
                bbox.setBottom(margin);
            }
            if (isAttributeDefined(CommonAttributes.MARGIN_TOP, CommonStyleAttributes.MARGIN_TOP)) {
                float margin = getDimensionAttribute(CommonAttributes.MARGIN_TOP, true, CommonStyleAttributes.MARGIN_TOP);
                bbox.setTop(oldBox.getTop() - margin);
            }
            importedPage.setBoundingBox(bbox);
            float left = bbox.getLeft() * -1;
            float bottom = bbox.getBottom() * -1;
            if (rotate) {
                importedPage.setMatrix(0.0F, 1.0F, -1.0F, 0.0F, oldBox.getHeight(), 0.0F);
                left *= -1.0F;
            }
            if (isAttributeDefined(CommonAttributes.IMAGE_X, CommonStyleAttributes.IMAGE_X)) {
                left = getFloatAttribute(CommonAttributes.IMAGE_X, true, CommonStyleAttributes.IMAGE_X);
            }
            if (isAttributeDefined(CommonAttributes.IMAGE_Y, CommonStyleAttributes.IMAGE_Y)) {
                bottom = getFloatAttribute(CommonAttributes.IMAGE_Y, true, CommonStyleAttributes.IMAGE_Y);
            }
            writer.getDirectContentUnder().addTemplate(importedPage, left, bottom);
        } catch (IllegalArgumentException ex) {
            throw new DocumentHandlerException(locator(), "The page #" + page + " could not be loaded from document " + source + "'.", ex);
        } catch (IOException ex) {
            throw new DocumentHandlerException(locator(), "Unable to load overlay document from location '" + source + "': " + ex.getMessage(), ex);
        }
    }
