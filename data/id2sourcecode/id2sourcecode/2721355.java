    private Set<String> doIndex() {
        File directory = ph.getDataLayout().getOpenDir();
        if (!directory.exists()) {
            return null;
        }
        for (File xmlFile : directory.listFiles(XMLFilenameFilter.getInstance())) {
            indexXMLFile(xmlFile);
        }
        Set<String> containerIds = new HashSet<String>(idToPath.keySet());
        Set<String> svnBasedContainersWithoutXPXs = new HashSet<String>();
        for (File subDirectory : directory.listFiles(DirectoryFilter.getInstance())) {
            String pathToXPXFile = subDirectory.getAbsolutePath() + XMLifier.XML_EXTENSION;
            File XPXFile = new File(pathToXPXFile);
            if (!XPXFile.exists() && subDirectory.getName().length() == 18) {
                if (ph.getDataSource().getVcsProvider() != null) {
                    logger.log(Level.WARNING, "Found a container that does not have an XPX file - " + subDirectory + ". As this is a VCS based datasource, to fix the issue the container is being deleted.");
                    svnBasedContainersWithoutXPXs.add(subDirectory.getAbsolutePath());
                } else {
                    logger.log(Level.WARNING, "Found a container that does not have an XPX file - " + subDirectory + ". Container not being loaded");
                    indexDirectory(subDirectory);
                }
            } else {
                indexDirectory(subDirectory);
            }
        }
        if (svnBasedContainersWithoutXPXs.size() > 0) {
            for (String pathToDelete : svnBasedContainersWithoutXPXs) {
                File file = new File(pathToDelete);
                if (file.isDirectory()) {
                    boolean hasSvnDir = false;
                    for (String filename : file.list()) {
                        if (filename.equals(".svn")) {
                            hasSvnDir = true;
                        }
                    }
                    if (!hasSvnDir) {
                        FileUtils.deleteDir(file);
                    }
                }
            }
        }
        return containerIds;
    }
