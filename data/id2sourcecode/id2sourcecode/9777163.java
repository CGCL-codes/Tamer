    public void readPostscript(PdfTemplate template) throws IOException, DocumentException {
        setTemplateData(template);
        template.setWidth(width());
        template.setHeight(height());
        InputStream is = null;
        try {
            if (rawData == null) {
                is = url.openStream();
            } else {
                is = new java.io.ByteArrayInputStream(rawData);
            }
            MetaDoPS meta = new MetaDoPS(is, template);
            meta.readAll();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
