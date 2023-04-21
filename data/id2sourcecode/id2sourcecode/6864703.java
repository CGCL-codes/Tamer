    private static long getTagDate() throws ConfigurationException {
        long tagDate = 0;
        boolean found = false;
        java.net.URL url = Resources.getResource(LICENSE_MARKER_FILE);
        if (url != null) {
            try {
                URLConnection conn;
                conn = url.openConnection();
                if (conn instanceof JarURLConnection) {
                    JarURLConnection juc = (JarURLConnection) conn;
                    JarEntry tagEntry = juc.getJarEntry();
                    if (tagEntry != null) {
                        found = true;
                        long entryDate = tagEntry.getTime();
                        if (entryDate != -1) {
                            found = true;
                            tagDate = entryDate;
                        } else {
                            throw new TBException("cannot read license information from jar file");
                        }
                    }
                } else if (conn.getClass().getName().indexOf("eclipse") != -1) {
                    return new Date().getTime() - 1000000L;
                } else {
                    String fileName = url.getFile();
                    fileName = fileName.replaceAll("%20", " ");
                    File tagFile = new File(fileName);
                    if (tagFile.exists()) {
                        found = true;
                        tagDate = tagFile.lastModified();
                    } else {
                        throw new TBException("cannot read license information from file");
                    }
                }
            } catch (IOException e) {
                logger.debug(e);
            }
        }
        if (!found) {
            throw new TBException("cannot find license information");
        }
        return tagDate;
    }
