    public static void writeWrapperCommand(String command) throws IOException, IllegalStateException {
        String commandFilename = WrapperPropertyUtil.getStringProperty("wrapper.commandfile", null);
        if (commandFilename == null) {
            throw new IllegalStateException("The wrapper.commandfile property has not been configured.");
        }
        File commandFile = new File(commandFilename);
        if (commandFile.exists()) {
            System.out.println(Main.getRes().getString("WARNING - Command file already exists when trying to write a new command: {0}", commandFile.toString()));
        }
        writeTextFile(commandFile, command);
    }
