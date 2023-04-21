    private void doImports() throws Exception {
        for (Import file : imports) {
            if (file.getPath() != null) {
            } else if (file.getFile() != null) {
                File f = new File(file.getFile());
                if (f.exists()) {
                    processFile(new FileInputStream(f));
                } else {
                    log("Missing file: " + file.getFile());
                    throw new FileNotFoundException(file.getFile());
                }
            } else if (file.getUrl() != null) {
                URL url = new URL(file.getUrl());
                InputStream is = url.openStream();
                processFile(is);
                is.close();
            } else {
                log("Import needs url or file");
                throw new BuildException("Import needs url or file");
            }
        }
    }
