    private Gedcom createGedcomFile(String fileNameX, boolean createSubmitter, Gedcom gedcomA, boolean display) {
        Origin originX = null;
        File fileX = new File(fileNameX);
        if (fileX.exists()) {
            if (display) log.write("   File " + fileNameX + " already exists.");
            fileX.delete();
            if (display) log.write("   Deleting file " + fileNameX + "...");
        }
        try {
            originX = Origin.create(new URL("file", "", fileX.getAbsolutePath()));
        } catch (MalformedURLException e) {
            log.write("URLexception:" + e);
            return null;
        }
        Gedcom gedcomX = new Gedcom(originX);
        gedcomX.setEncoding(gedcomA.getEncoding());
        gedcomX.setLanguage(gedcomA.getLanguage());
        gedcomX.setPassword(gedcomA.getPassword());
        gedcomX.setPlaceFormat(gedcomA.getPlaceFormat());
        if (createSubmitter) {
            try {
                Submitter sub = (Submitter) gedcomX.createEntity(Gedcom.SUBM, gedcomX.getNextAvailableID(Gedcom.SUBM));
                sub.addDefaultProperties();
                gedcomX.setSubmitter(sub);
            } catch (GedcomException e) {
                log.write("GedcomException:" + e);
                return null;
            }
        }
        return gedcomX;
    }
