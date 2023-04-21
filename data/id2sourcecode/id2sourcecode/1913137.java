    public void send(Collection<Document> documents, String description, XdsContentCode contentCode, String directory, String mediaDescription) throws DocumentException {
        String workingDir = directory;
        String xdmDir = workingDir + File.separator + "IHE_XDM";
        new File(xdmDir).mkdir();
        try {
            copyFile(new File(XdmMessenger.class.getResource("xdm/README.TXT").getPath()), new File(workingDir + File.separator + "README.TXT"));
        } catch (Exception e) {
            log.error("Error copying README.TXT", e);
        }
        XdsDocumentSource xdsSource = new XdsDocumentSource(connection, auditTrail);
        XdsMessenger xdsMessenger = new XdsMessenger(connection, false);
        XdsSubmissionSet submissionSet = null;
        try {
            submissionSet = xdsSource.createSubmissionSetMetadata(documents, description, contentCode);
        } catch (IheConfigurationException e) {
            log.error("Error generating the submission set metadata", e);
        }
        ArrayList documentFileNames = new ArrayList();
        int count = 1;
        for (Iterator i = documents.iterator(); i.hasNext(); count++) {
            String currentDir = xdmDir + File.separator + "SUBSET" + count;
            new File(currentDir).mkdir();
            Document doc = (Document) i.next();
            XdsDocumentEntry entry = null;
            try {
                entry = xdsSource.createDocumentMetadata(doc);
            } catch (IheConfigurationException e) {
                log.error("Error generating the document metadata", e);
            }
            String suffix = mimeTypeMap.getSuffix(entry.getMimeType());
            String embeddedFilename = suffix + "." + suffix;
            entry.setContent(doc.getContents());
            entry.setEntryUuid("doc" + count);
            entry.setUri(embeddedFilename);
            List documentEntries = new ArrayList<XdsDocumentEntry>();
            documentEntries.add(entry);
            SOAPMessage xdsSubmission = xdsMessenger.createSubmitObjectsRequest(submissionSet, documentEntries);
            try {
                StringWriter content = new StringWriter();
                XMLSerializer ser = (XMLSerializer) SerializerFactory.getSerializerFactory(Method.XML).makeSerializer(content, new OutputFormat()).asDOMSerializer();
                ser.setNamespaces(true);
                ser.serialize((Element) xdsSubmission.getSOAPBody().getChildNodes().item(0));
                File file = new File(currentDir + File.separator + "METADATA.XML");
                FileWriter fw = new FileWriter(file);
                fw.write(content.toString());
                fw.flush();
                fw.close();
            } catch (Exception e) {
                log.error("Error writing metatdata" + e);
            }
            String contentsFilename = currentDir + File.separator + embeddedFilename;
            if ("text/x-cda-r2+xml".equals(doc.getMimeType()) || "text/xml".equals(doc.getMimeType())) {
                try {
                    doc.setContents(doc.getContents().toString().replaceFirst("(?s)(?i)<?xml-stylesheet.*?>", "xml-stylesheet type=\"text/xsl\" href=\"IMPCDAR2.XSL\"?>"));
                    BufferedWriter file = new BufferedWriter(new FileWriter(contentsFilename));
                    file.write(doc.getContents().toString());
                    file.close();
                    copyFile(new File(XdmMessenger.class.getResource("xdm/IMPL_CDAR2.xsl").getPath()), new File(currentDir + File.separator + "IMPCDAR2.XSL"));
                    InputStream contentsStream = new BufferedInputStream(new FileInputStream(contentsFilename));
                } catch (Exception e) {
                    log.error("Error writing xml contents", e);
                }
            } else {
                try {
                    URL url = new URL(doc.getUri());
                    byte buf[] = new byte[1000];
                    InputStream input = url.openStream();
                    FileOutputStream fout = new FileOutputStream(contentsFilename);
                    int ch;
                    int currentCount = 0;
                    while (true) {
                        int n = input.read(buf, 0, 1000);
                        if (n == -1) break;
                        fout.write(buf, 0, n);
                        currentCount += n;
                    }
                    fout.close();
                    input.close();
                } catch (Exception e) {
                    log.error("Error retrieving url contents", e);
                }
            }
            documentFileNames.add(contentsFilename.substring(workingDir.length() + 1));
            if (auditTrail != null) {
                ParticipantObject logobject = new ParticipantObject(doc);
                auditTrail.recordExported(logobject, mediaDescription);
            }
        }
        try {
            Properties props = new Properties();
            String vmroot = new File(XdmMessenger.class.getResource("xdm").getPath()).getAbsolutePath();
            props.setProperty("resource.loader", "file");
            props.setProperty("file.resource.loader.path", vmroot);
            Velocity.init(props);
            VelocityContext context = new VelocityContext();
            PatientDescriptor patient = documents.iterator().next().getPatientDescriptor();
            context.put("patient", patient);
            Date date = new Date();
            Format formatter = new SimpleDateFormat("MMMM d, yyyy");
            context.put("date", formatter.format(date));
            if (patient.getBirthDateTime() != null) {
                context.put("dob", formatter.format(patient.getBirthDateTime()));
            }
            context.put("docs", documentFileNames);
            Template template = Velocity.getTemplate("INDEX.HTM.vm");
            FileWriter writer = new FileWriter(workingDir + File.separator + "INDEX.HTM");
            if (template != null) {
                template.merge(context, writer);
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.error("Error generating INDEX.HTM", e);
        }
    }
