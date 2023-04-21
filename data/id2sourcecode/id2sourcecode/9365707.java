    private void packLog4jConfigFile(PackerConfig config, JarOutputStream jarOutputStream) throws IOException {
        String log4jFile = config.getLog4jResourcePath() != null ? config.getLog4jResourcePath() : LOG4J_CONFIG_FILE;
        JarEntry log4jEntry = null;
        InputStream is = null;
        boolean loadDefault = true;
        if (config.getLog4jResourcePath() != null) {
            try {
                is = new FileInputStream(log4jFile);
                loadDefault = false;
                log4jEntry = new JarEntry(new File(log4jFile).getName());
            } catch (FileNotFoundException e) {
                System.out.println("The provided log4j config file doesn't exist: " + e.getMessage() + "\nUsing the default one.");
            }
        }
        if (loadDefault) {
            URL url = this.getClass().getResource(LOG4J_CONFIG_FILE);
            try {
                is = url.openStream();
                log4jEntry = new JarEntry(new File(url.getFile()).getName());
            } catch (IOException e) {
                throw new BuildInstallerException("Unable to load the default log4j config file :" + e.getMessage());
            }
        }
        try {
            jarOutputStream.putNextEntry(log4jEntry);
            PackerUtils.copyStream(is, jarOutputStream);
        } finally {
            is.close();
        }
        jarOutputStream.closeEntry();
    }
