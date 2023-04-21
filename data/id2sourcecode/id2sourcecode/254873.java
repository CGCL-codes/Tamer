    public void execute() throws BuildException {
        File versionDir = new File(getProject().getBaseDir(), localDir);
        if (!versionDir.exists() || !versionDir.isDirectory()) {
            System.out.println("Dir: " + versionDir.toString() + " does not Exist. Cannot Update Dist JNLP Files!");
        }
        File files[] = versionDir.listFiles();
        try {
            DocumentBuilderFactory defaultFactory = DocumentBuilderFactory.newInstance();
            defaultFactory.setValidating(false);
            defaultFactory.setNamespaceAware(true);
            builder = defaultFactory.newDocumentBuilder();
        } catch (Throwable _e) {
            _e.printStackTrace();
            throw new BuildException("Error Creating XML Parser");
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) continue;
            if (files[i].getName().endsWith(".jnlp")) {
                fixCodeBase(files[i]);
            }
        }
    }
