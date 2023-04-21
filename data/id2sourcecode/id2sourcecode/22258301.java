    public void readWMF(PdfTemplate template) throws IOException, DocumentException {
        setTemplateData(template);
        template.setWidth(getWidth());
        template.setHeight(getHeight());
        InputStream is = null;
        try {
            if (rawData == null) {
                is = url.openStream();
            } else {
                is = new java.io.ByteArrayInputStream(rawData);
            }
            MetaDo meta = new MetaDo(is, template);
            meta.readAll();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
