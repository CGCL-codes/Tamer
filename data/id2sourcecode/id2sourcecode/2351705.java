    private String getConfigDTD() throws IOException {
        File currentDir = new File(System.getProperty("user.dir"));
        File voyagerDir = new File(currentDir, "mps/voyager");
        if (!voyagerDir.exists()) {
            voyagerDir = new File(currentDir, "voyager");
        }
        if (!voyagerDir.exists()) {
            voyagerDir = currentDir;
        }
        Reader reader = null;
        StringWriter sw = new StringWriter();
        File dtdFile = new File(voyagerDir, SCHEMA_FILE);
        if (!dtdFile.exists()) {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(SCHEMA_RESOURCE);
            if (stream == null) {
                throw new IllegalStateException("can't find schema resource " + SCHEMA_RESOURCE + " or schema file " + dtdFile);
            } else {
                reader = new InputStreamReader(stream);
            }
        } else {
            reader = new FileReader(dtdFile);
        }
        char[] buf = new char[1024];
        int read;
        try {
            while ((read = reader.read(buf)) != -1) {
                sw.write(buf, 0, read);
            }
        } finally {
            reader.close();
        }
        return sw.toString();
    }
