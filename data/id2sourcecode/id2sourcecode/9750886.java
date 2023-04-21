    private Gedcom getGedcomFromUser(String msg) {
        Gedcom gedcomX = null;
        Origin originX = null;
        GedcomReader readerX;
        File fileX = report.getFileFromUser(msg, Action2.TXT_OK);
        if (fileX == null) return null;
        try {
            originX = Origin.create(new URL("file", "", fileX.getAbsolutePath()));
        } catch (MalformedURLException e) {
            log.write("URLexception:" + e);
            return null;
        }
        try {
            readerX = new GedcomReader(originX);
        } catch (IOException e) {
            log.write("IOexception:" + e);
            return null;
        }
        try {
            gedcomX = readerX.read();
        } catch (GedcomIOException e) {
            log.write("GedcomIOexception:" + e);
            log.write("At line:" + e.getLine());
            return null;
        }
        log.write("   LinesRead: " + readerX.getLines());
        List warnings = readerX.getWarnings();
        log.write("   Warnings: " + warnings.size());
        for (Iterator it = warnings.iterator(); it.hasNext(); ) {
            String wng = (String) it.next().toString();
            log.write("   " + wng);
        }
        linkGedcom(gedcomX);
        return gedcomX;
    }
