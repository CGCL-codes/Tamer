    public static void importProgrammes(URL url) throws FileNotFoundException, IOException, DuplicateURIException, SimalException {
        HSSFWorkbook wb = new HSSFWorkbook(url.openStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = sheet.getRow(0);
        String title = getNullSafeStringValue(row, 1);
        if (!title.equals(VALID_PROGRAMMES_FILE_ID)) {
            throw new SimalException(url + " is not a valid PIMS programme export file");
        }
        int lastRow = sheet.getLastRowNum();
        for (int i = 1; i <= lastRow; i++) {
            Document doc;
            Element cat;
            try {
                doc = createRdfDocument();
                cat = doc.createElementNS(Doap.getURI(), "category");
            } catch (ParserConfigurationException e1) {
                throw new SimalException("Unable to create XML document for import");
            }
            row = sheet.getRow(i);
            int id = getNullSafeIntValue(row, 0);
            cat.setAttributeNS(RDF.getURI(), "about", getCategoryURI(id));
            String value = getNullSafeStringValue(row, 1);
            cat.setAttributeNS(RDFS.getURI(), "label", value);
            doc.getDocumentElement().appendChild(cat);
            serialise(doc);
            SimalRepositoryFactory.getInstance().addRDFXML(doc);
        }
    }
