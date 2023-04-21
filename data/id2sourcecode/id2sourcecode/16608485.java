    public static void importProjects(URL url) throws FileNotFoundException, IOException, DuplicateURIException, SimalException {
        HSSFWorkbook wb = new HSSFWorkbook(url.openStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = sheet.getRow(0);
        String title = getNullSafeStringValue(row, 2);
        if (!title.equals(VALID_PROJECTS_FILE_ID)) {
            throw new SimalException(url + " is not a valid PIMS project export file");
        }
        int errorsOccurred = 0;
        StringBuffer errorReports = new StringBuffer();
        int lastRow = sheet.getLastRowNum();
        for (int i = 1; i <= lastRow; i++) {
            Document doc;
            Element doap;
            try {
                doc = createRdfDocument();
                doap = doc.createElementNS(Doap.getURI(), "Project");
            } catch (ParserConfigurationException e1) {
                throw new SimalException("Unable to create XML document for import");
            }
            row = sheet.getRow(i);
            int id = getNullSafeIntValue(row, 0);
            doap.setAttributeNS(RDF.getURI(), "about", getProjectURI(id));
            String name = getNullSafeStringValue(row, 2);
            Element elem = doc.createElementNS(Doap.getURI(), "name");
            elem.setTextContent(name);
            doap.appendChild(elem);
            String value = getNullSafeStringValue(row, 4);
            elem = doc.createElementNS(Doap.getURI(), "description");
            elem.setTextContent(value);
            doap.appendChild(elem);
            value = getNullSafeStringValue(row, 6);
            if (value.length() != 0 && !value.equals("tbc")) {
                elem = doc.createElementNS(Doap.getURI(), "homepage");
                elem.setAttributeNS(RDF.getURI(), "resource", value);
                elem.setAttributeNS(RDFS.getURI(), "label", "Homepage");
                doap.appendChild(elem);
            }
            value = getCategoryURI(getNullSafeIntValue(row, 1));
            elem = doc.createElementNS(Doap.getURI(), "category");
            elem.setAttributeNS(RDF.getURI(), "resource", value);
            doap.appendChild(elem);
            doc.getDocumentElement().appendChild(doap);
            try {
                SimalRepositoryFactory.getProjectService().createProject(doc);
            } catch (SimalRepositoryException e) {
                errorReports.append("Error when importing project named '" + name + "': ");
                errorReports.append(e.getMessage());
                errorReports.append(NEW_LINE);
                errorsOccurred++;
                if (errorsOccurred > MAX_IMPORT_ERRORS) {
                    throw new SimalException("Too many errors (" + MAX_IMPORT_ERRORS + ")" + NEW_LINE + errorReports.toString());
                }
            }
        }
        if (errorsOccurred > 0) {
            throw new SimalException("Import resulted in " + errorsOccurred + " errors:" + NEW_LINE + errorReports.toString());
        }
    }
