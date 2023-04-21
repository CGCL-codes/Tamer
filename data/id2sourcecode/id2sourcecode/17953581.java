    private void init() {
        URL url = getClass().getClassLoader().getResource(HELP_FILE);
        if (url == null) {
            System.err.println("Cannot locate help definition resource " + HELP_FILE);
            return;
        }
        String path;
        Digester digester = new Digester();
        digester.setValidating(false);
        path = "Help/Topic";
        digester.addObjectCreate(path, "org.sqsh.HelpTopic");
        digester.addSetNext(path, "addTopic", "org.sqsh.HelpTopic");
        digester.addCallMethod(path, "setTopic", 1, new Class[] { java.lang.String.class });
        digester.addCallParam(path, 0, "name");
        path = "Help/Topic/Description";
        digester.addCallMethod(path, "setDescription", 1, new Class[] { java.lang.String.class });
        digester.addCallParam(path, 0);
        path = "Help/Topic/HelpText";
        digester.addCallMethod(path, "setHelp", 1, new Class[] { java.lang.String.class });
        digester.addCallParam(path, 0);
        digester.push(this);
        InputStream in = null;
        try {
            in = url.openStream();
            digester.parse(in);
        } catch (Exception e) {
            System.err.println("Failed to parse internal command file '" + HELP_FILE + "': " + e.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }
