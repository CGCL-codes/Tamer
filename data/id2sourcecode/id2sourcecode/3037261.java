    @Override
    public void save() throws IOException {
        if (fFile == null || !fFile.exists()) {
            return;
        }
        Document doc = new Document();
        Element root = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_MANIFEST);
        doc.setRootElement(root);
        Element elementName = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_NAME);
        elementName.setText(getName());
        root.addContent(elementName);
        Element elementDescription = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_DESCRIPTION);
        elementDescription.setText(getDescription());
        root.addContent(elementDescription);
        if (fKeyThumbnailPath != null) {
            Element elementKeyThumb = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_KEY_THUMBNAIL);
            elementKeyThumb.setText(fKeyThumbnailPath);
            root.addContent(elementKeyThumb);
        }
        String manifest = JDOMUtils.write2XMLString(doc);
        String model = ZipUtils.extractZipEntry(fFile, TemplateManager.ZIP_ENTRY_MODEL);
        File tmpFile = File.createTempFile("architemplate", null);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tmpFile));
        ZipOutputStream zOut = new ZipOutputStream(out);
        ZipUtils.addStringToZip(manifest, TemplateManager.ZIP_ENTRY_MANIFEST, zOut);
        ZipUtils.addStringToZip(model, TemplateManager.ZIP_ENTRY_MODEL, zOut);
        Image[] images = getThumbnails();
        int i = 1;
        for (Image image : images) {
            ZipUtils.addImageToZip(image, TemplateManager.ZIP_ENTRY_THUMBNAILS + i++ + ".png", zOut, SWT.IMAGE_PNG, null);
        }
        zOut.flush();
        zOut.close();
        fFile.delete();
        FileUtils.copyFile(tmpFile, fFile, false);
        tmpFile.delete();
    }
