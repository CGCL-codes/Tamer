    private final boolean validateFileSets(long afterI, long beforeI, java.util.Vector validSealsO, java.util.Vector invalidSealsO) {
        boolean validR = true;
        long after = afterI, before = beforeI;
        String directory = getArchiveDirectory();
        com.rbnb.compat.File archive = new com.rbnb.compat.File(directory);
        Directory asDirectory = new Directory(archive);
        com.rbnb.compat.File[] files;
        try {
            if (!archive.exists()) {
                throw new java.lang.IllegalStateException("Cannot find archive directory " + directory);
            } else if ((files = asDirectory.listFiles()) == null) {
                throw new java.lang.IllegalStateException("Archive location " + directory + " is not a directory.");
            }
        } catch (java.io.IOException e) {
            throw new java.lang.IllegalStateException("Archive in " + directory + " is in a bad state.\n" + e.getMessage());
        }
        boolean summary = false;
        java.util.Vector fileSets = new java.util.Vector();
        for (int idx = 0; idx < files.length; ++idx) {
            try {
                String name = files[idx].getName();
                Directory fAsDirectory = new Directory(files[idx]);
                if (name.equalsIgnoreCase("summary.rbn")) {
                    summary = true;
                } else if (name.substring(0, 2).equalsIgnoreCase("FS") && (fAsDirectory.listFiles() != null)) {
                    long fsIndex = 0;
                    try {
                        fsIndex = Long.parseLong(name.substring(2));
                    } catch (java.lang.NumberFormatException e) {
                        continue;
                    }
                    int lo, hi, idx1;
                    for (lo = 0, hi = fileSets.size() - 1, idx1 = (lo + hi) / 2; (lo <= hi); idx1 = (lo + hi) / 2) {
                        com.rbnb.compat.File other = (com.rbnb.compat.File) fileSets.elementAt(idx1);
                        long oIndex = Long.parseLong(other.getName().substring(2));
                        if (fsIndex < oIndex) {
                            hi = idx1 - 1;
                        } else if (fsIndex > oIndex) {
                            lo = idx1 + 1;
                        } else {
                            break;
                        }
                    }
                    fileSets.insertElementAt(files[idx], lo);
                }
            } catch (java.io.IOException e) {
            }
        }
        for (int idx = 0, endIdx = fileSets.size(); idx < endIdx; ++idx) {
            com.rbnb.compat.File fileSetFile = (com.rbnb.compat.File) fileSets.elementAt(idx);
            try {
                Seal theSeal = Seal.validate(fileSetFile.getAbsolutePath(), after, before);
                if (theSeal == null) {
                    throw new com.rbnb.api.InvalidSealException(theSeal, after, before);
                } else {
                    validSealsO.addElement(fileSetFile);
                    validSealsO.addElement(theSeal);
                    after = theSeal.getAsOf();
                }
            } catch (com.rbnb.api.InvalidSealException e) {
                invalidSealsO.addElement(fileSetFile);
                invalidSealsO.addElement(e);
                validR = false;
            }
        }
        if (!summary) {
            validR = false;
        }
        return (validR);
    }
