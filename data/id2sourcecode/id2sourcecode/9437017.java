    public Commands getCommands() {
        String root = "commands";
        Map methods = new HashMap();
        Map params = new HashMap();
        params.put("commands/command", "name");
        params.put("commands/command", "alias");
        methods.put("commands/command/note", "setNote");
        methods.put("commands/command/run", "setRun");
        DigesterUtil.DigesterUtilDetails ruleDetails = new DigesterUtil.DigesterUtilDetails(root, methods, params);
        InputStream xml = null;
        try {
            xml = new ByteArrayInputStream(FileUtils.readFileToString(new File(PropsUtil.get("command.file.path"))).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Commands cs = new Commands();
        cs = (Commands) DigesterUtil.digest(xml, cs, ruleDetails);
        return cs;
    }
