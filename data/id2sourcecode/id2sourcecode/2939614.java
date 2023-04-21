    public void deleteIndex() throws IndexException {
        boolean isDeleted = true;
        if (parameters == null) return;
        File dir = null;
        try {
            dir = new File(((URL) parameters.get(Constants.INDEX_LOCATION_URL)).toURI());
        } catch (URISyntaxException use) {
            dir = new File(((URL) parameters.get(Constants.INDEX_LOCATION_URL)).getFile());
        }
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f.isDirectory()) {
                    File[] subFiles = f.listFiles();
                    for (int j = 0; j < subFiles.length; j++) {
                        File sf = subFiles[j];
                        sf.delete();
                    }
                }
                f.delete();
            }
        }
        isDeleted = dir.delete();
        if (!isDeleted) {
            throw new IndexException("Can't delete directory" + dir.getAbsolutePath());
        }
    }
