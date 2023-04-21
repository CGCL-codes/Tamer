    public static void importProjectContacts(URL url) throws IOException, SimalException {
        HSSFWorkbook wb = new HSSFWorkbook(url.openStream());
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = sheet.getRow(0);
        String title = getNullSafeStringValue(row, 2);
        if (!title.equals(VALID_PROJECT_CONTACTS_FILE_ID)) {
            throw new SimalException(url + " is not a valid PIMS project contact export file");
        }
        StringBuffer errorReports = new StringBuffer();
        int errorsOccurred = 0;
        int lastRow = sheet.getLastRowNum();
        for (int i = 1; i <= lastRow; i++) {
            Document doc;
            Element project;
            try {
                doc = createRdfDocument();
                project = doc.createElementNS(Doap.getURI(), "Project");
            } catch (ParserConfigurationException e1) {
                throw new SimalException("Unable to create XML document for import");
            }
            row = sheet.getRow(i);
            int projectId = getNullSafeIntValue(row, 1);
            project.setAttributeNS(RDF.getURI(), "about", getProjectURI(projectId));
            Element person = doc.createElementNS(Foaf.getURI(), "Person");
            int id = getNullSafeIntValue(row, 0);
            person.setAttributeNS(RDF.getURI(), "about", getPersonURI(id));
            String name = getNullSafeStringValue(row, 2);
            Element elem = doc.createElementNS(Foaf.getURI(), "name");
            elem.setTextContent(name);
            person.appendChild(elem);
            String email = getNullSafeStringValue(row, 6);
            if (email.contains("@")) {
                if (!email.startsWith("mailto:")) {
                    email = "mailto:" + email;
                }
                elem = doc.createElementNS(Foaf.getURI(), "mbox");
                elem.setAttributeNS(RDF.getURI(), "resource", email);
                person.appendChild(elem);
            } else {
                logger.info("Contact in PIMS import has a strange looking email: " + email);
            }
            String role = getNullSafeStringValue(row, 3);
            if (role.equals("Programme Stream Manager") || role.equals("Programme Strand Manager") || role.equals("Programme Manager")) {
                elem = doc.createElementNS(Doap.getURI(), "helper");
                elem.appendChild(person);
            } else if (role.equals("Project Director") || role.equals("Project Manager")) {
                elem = doc.createElementNS(Doap.getURI(), "maintainer");
                elem.appendChild(person);
            } else if (role.equals("Project Team Member")) {
                elem = doc.createElementNS(Doap.getURI(), "developer");
                elem.appendChild(person);
            } else {
                logger.warn("Got a person (" + name + ") with an unkown role, adding as helper: " + role);
            }
            project.appendChild(elem);
            doc.getDocumentElement().appendChild(project);
            serialise(doc);
            try {
                SimalRepositoryFactory.getProjectService().createProject(doc);
            } catch (SimalRepositoryException e) {
                errorReports.append("Error when importing person named '" + name + "': ");
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
