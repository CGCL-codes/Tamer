    public void execute(List<String> arguments) throws ShellException {
        if (arguments.size() != 1) error("Incorrect format. Expected 'cat FILENAME'.");
        String path = arguments.get(0);
        File file = getFile(path);
        if (file == null) error("Unable to locate to '%s'.", path);
        if (file.isDirectory()) error("Unable to cat a directory.");
        if (!file.exists() || file.lastModified() == 0) error("File '%s' does not exist.", path);
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = "";
            int lineNumber = 1;
            while ((line = in.readLine()) != null) shell.writer().format("%d:  %s\n", lineNumber++, line);
            in.close();
        } catch (Exception e) {
            error(e, "Error reading file '%s'.", file.getName());
        }
    }
