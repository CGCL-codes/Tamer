    public void importCDADocument(File xmlFile, DateTimeModel created, DocumentClientDto document, PatientModel patient) {
        try {
            File xsl = new File("C:\\temp\\test_files\\tiani_cda.xsl");
            File htmlFile = SystemUtil.getTemporaryFile("htm");
            XsltUtil.createTextFile(xmlFile, xsl, htmlFile);
            String textContents = FileSystemUtil.getTextContents(htmlFile);
            int startPos = textContents.indexOf("<body>");
            textContents = "<html>" + textContents.substring(startPos);
            textContents = textContents.replace("<left>", "");
            textContents = textContents.replace("</left>", "");
            FileSystemUtil.createFile(htmlFile.getAbsolutePath(), textContents);
            FileUtils.copyFile(htmlFile, new File("C:\\temp\\test_files\\cda.html"));
            long fileId = SystemService.storeFile(htmlFile.getAbsolutePath(), ContentTypeReference.TEXTHTML.getRefId());
            FormModel form = ClinicalServer.prepareNewForm(patient.getId(), patient.getVisitId(), 40036996L);
            form.setRecordUuid(document.getCreationTime());
            form.setFileId(fileId);
            System.out.println("uploading CDA " + patient.getId() + " " + patient.getLastName() + " visit id " + patient.getVisitId());
            long formId = ClinicalServer.store(patient.getId(), patient.getVisitId(), form);
            System.out.println("form id " + formId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
