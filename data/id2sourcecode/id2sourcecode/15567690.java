    public void wirteCatalog(QuestionCatalog catalog, OutputStream out) throws IOException {
        ZipOutputStream zOut = new ZipOutputStream(out);
        try {
            ZipEntry entry = new ZipEntry("info.xml");
            JAXBContext context = JAXBContext.newInstance(QuestionCatalog.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(m.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            zOut.putNextEntry(entry);
            m.marshal(catalog, zOut);
            for (int i = 0; i < catalog.getQuestions().size(); i++) {
                this.putQuestion(catalog.getQuestions().get(i), zOut);
            }
        } catch (JAXBException ex) {
            Logger.getLogger(CatalogWriter.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex);
        }
        zOut.close();
    }
