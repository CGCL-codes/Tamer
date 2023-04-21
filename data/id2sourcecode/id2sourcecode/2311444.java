    private void packLog4jConfigFile(PackerConfiguration config, JarOutputStream jarOutputStream) throws IOException {
        String log4jFile = config.getLog4jFile() != null ? config.getLog4jFile() : LOG4J_CONFIG_FILE;
        JarEntry log4jEntry = null;
        InputStream is = null;
        boolean loadDefault = true;
        if (config.getLog4jFile() != null) {
            try {
                is = new FileInputStream(log4jFile);
                loadDefault = false;
                log4jEntry = new JarEntry(new File(log4jFile).getName());
            } catch (FileNotFoundException e) {
                System.out.println("The provided log4j config file doesn't exist: " + e.getMessage() + "\nUsing the default one : ");
            }
        }
        if (loadDefault) {
            URL url = this.getClass().getResource(LOG4J_CONFIG_FILE);
            try {
                is = url.openStream();
                log4jEntry = new JarEntry(new File(url.getFile()).getName());
            } catch (IOException e) {
                System.out.println("Unable to load the default log4j config file : " + e.getMessage());
                throw new BuildException(e);
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
