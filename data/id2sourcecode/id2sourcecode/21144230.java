    private Eml copyMetadata(String shortname, File emlFile) throws ImportException {
        File emlFile2 = dataDir.resourceEmlFile(shortname, null);
        try {
            FileUtils.copyFile(emlFile, emlFile2);
        } catch (IOException e1) {
            log.error("Unnable to copy EML File", e1);
        }
        Eml eml = null;
        try {
            InputStream in = new FileInputStream(emlFile2);
            eml = EmlFactory.build(in);
        } catch (FileNotFoundException e) {
            eml = new Eml();
        } catch (IOException e) {
            log.error(e);
        } catch (SAXException e) {
            log.error("Invalid EML document", e);
        }
        if (eml == null) {
            throw new ImportException("Invalid EML document");
        }
        return eml;
    }
