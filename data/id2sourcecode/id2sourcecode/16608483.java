    public static void importInstitutions(URL url) throws FileNotFoundException, IOException, DuplicateURIException, SimalException {
        HSSFWorkbook wb = new HSSFWorkbook(url.openStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = sheet.getRow(0);
        String title = getNullSafeStringValue(row, 1);
        if (!title.equals("name")) {
            throw new SimalException(url + " is not a valid PIMS project export file");
        }
        int lastRow = sheet.getLastRowNum();
        for (int i = 1; i <= lastRow; i++) {
            Document doc;
            Element foaf;
            try {
                doc = createRdfDocument();
                foaf = doc.createElementNS(Foaf.getURI(), "Organization");
            } catch (ParserConfigurationException e1) {
                throw new SimalException("Unable to create XML document for import");
            }
            row = sheet.getRow(i);
            int id = getNullSafeIntValue(row, 0);
            foaf.setAttributeNS(RDF.getURI(), "about", getOrganisationURI(id));
            String value = getNullSafeStringValue(row, 1);
            Element elem = doc.createElementNS(Foaf.getURI(), "name");
            elem.setTextContent(value);
            foaf.appendChild(elem);
            int projectId = getNullSafeIntValue(row, 2);
            elem = doc.createElementNS(Foaf.getURI(), "currentProject");
            elem.setAttributeNS(RDF.getURI(), "resource", getProjectURI(projectId));
            foaf.appendChild(elem);
            doc.getDocumentElement().appendChild(foaf);
            serialise(doc);
            SimalRepositoryFactory.getInstance().addRDFXML(doc);
        }
    }
