    public void init() {
        filenameprefix = ph.get("filenameprefix");
        seqnumberdigits = ph.getInteger("seqnumberdigits");
        if (filenameprefix.length() == 0) {
            throw new RuntimeException("Please give a file name prefix.");
        }
        boolean safemode = Boolean.parseBoolean(ph.get("safemode"));
        if (safemode) {
            String currentFileName = generateFileName(filenameprefix, filenamesuffix + 1, filenameext);
            File file = new File(currentFileName);
            if (file.exists()) {
                throw new RuntimeException("Network writer plugin - First file already exists, and safe mode was on.");
            }
        }
        seqnumbermax = ph.getInteger("seqnumbermax");
    }
