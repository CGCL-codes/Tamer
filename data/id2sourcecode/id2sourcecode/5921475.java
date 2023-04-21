    private void insertTemplateTree(String directory) {
        PrepareChangesForPersist prep = new PrepareChangesForPersist("admin");
        List<String> files = new ArrayList<String>();
        try {
            files = listFiles(directory, ".xml");
        } catch (MalformedURLException e1) {
            FacesMessages.instance().add(FacesMessage.SEVERITY_ERROR, "Template library couldn't be populated. ");
            return;
        } catch (IOException e1) {
            FacesMessages.instance().add(FacesMessage.SEVERITY_ERROR, "Template library couldn't be populated. ");
            return;
        }
        for (String filename : files) {
            try {
                log.info("Inserting templates from " + filename);
                InputStream istream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
                ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
                byte[] bytes = new byte[512];
                int readBytes;
                while ((readBytes = istream.read(bytes)) > 0) {
                    out.write(bytes, 0, readBytes);
                }
                byte[] data = out.toByteArray();
                istream.close();
                out.close();
                projectImport.storeTemplatesInLibrary(data);
            } catch (IOException e) {
                log.error("Failed to insert templates: " + e.getMessage());
                FacesMessages.instance().add(FacesMessage.SEVERITY_ERROR, "Template library couldn't be populated. ");
                return;
            } catch (SAXException e) {
                log.error("Failed to insert templates: " + e.getMessage());
                FacesMessages.instance().add(FacesMessage.SEVERITY_ERROR, "Template library couldn't be populated. ");
                return;
            }
        }
    }
