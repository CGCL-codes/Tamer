    public static File[] listConfigDBs() {
        File[] tmpRoots = File.listRoots();
        ArrayList tmp = new ArrayList();
        File[] roots = new File[tmpRoots.length + 1];
        System.arraycopy(tmpRoots, 0, roots, 0, tmpRoots.length);
        roots[roots.length - 1] = new File(".");
        for (int i = 0; i < roots.length; i++) {
            try {
                File root = roots[i];
                File subdir = new File(root, "TrackemEvents");
                if (!subdir.exists()) continue;
                if (!subdir.isDirectory()) continue;
                logger.finer("Looking for Event files in " + subdir.getCanonicalPath());
                File[] possibilities = subdir.listFiles();
                for (int j = 0; j < possibilities.length; j++) {
                    if (!possibilities[j].isDirectory()) continue;
                    File prop = new File(possibilities[j], "service.properties");
                    if (!prop.exists()) {
                        logger.fine("Folder " + possibilities[j] + " exists, but is not recognized as a Derby database.)");
                        continue;
                    }
                    logger.finer("Found event config database " + possibilities[j]);
                    tmp.add(possibilities[j]);
                }
            } catch (IOException e) {
                logger.fine("Error enumerating event files: \n" + e);
                continue;
            }
        }
        logger.finer("Returning possible events " + tmp);
        return (File[]) tmp.toArray(new File[tmp.size()]);
    }
