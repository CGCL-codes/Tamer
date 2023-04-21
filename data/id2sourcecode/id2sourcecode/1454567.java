    private File extractScript(String scriptName) throws IOException {
        int rnd = Math.abs(new Random().nextInt());
        String temporaryScriptName = "." + scriptName + "-" + rnd;
        File script = new File(configureDirectory, temporaryScriptName);
        Map<String, String> variables = makeVariables();
        Reader reader = new InterpolationFilterReader(new InputStreamReader(getClass().getResourceAsStream(scriptName), "UTF-8"), variables);
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(script), "UTF-8");
            try {
                IOUtil.copy(reader, writer);
            } finally {
                IOUtil.close(writer);
            }
        } finally {
            IOUtil.close(reader);
        }
        script.setExecutable(true);
        return script;
    }
