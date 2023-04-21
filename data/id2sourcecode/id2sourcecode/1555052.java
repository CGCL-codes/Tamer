    public static void main(String[] args) throws Exception {
        File configFile = new File("src/etc/conf/server.xml");
        ServerConfig config = new ServerConfig();
        config.load(configFile);
        File file = new File("src/site/commands.html");
        file.getParentFile().mkdirs();
        System.out.println("Exporting commands documentation from " + configFile + " to " + file);
        System.out.println("");
        TreeMap<String, Command> commands = new TreeMap<String, Command>();
        Iterator<Command> it = CommandManager.getInstance().getCommands(AccessLevel.ADMINISTRATOR);
        while (it.hasNext()) {
            Command command = it.next();
            commands.put(command.getAliases()[0], command);
        }
        for (ChannelConfig channel : config.getChannels()) {
            Iterator<FilterConfig> filters = channel.getFilters();
            while (filters.hasNext()) {
                FilterConfig filter = filters.next();
                if (!filter.isGlobal() && filter.getName().equals("command")) {
                    String cls = filter.getProperties().getProperty("class");
                    Command command = (Command) Class.forName(cls).newInstance();
                    commands.put(command.getAliases()[0], command);
                }
            }
        }
        PrintWriter out = new PrintWriter(new FileWriter(file));
        for (Command command : commands.values()) {
            String alias = command.getAliases()[0];
            out.println("<h2 id=\"command-" + alias + "\">" + alias + "</h2>");
            out.println();
            out.println("<p>" + command.getDescription(Locale.ENGLISH) + "</p>");
            out.println();
            out.println("<div><b>Usage:</b> <tt>" + htmlizeUsage(command.getUsage(Locale.ENGLISH)) + "</tt></div>");
            if (command.getAliases().length > 1) {
                out.println("<div><b>Aliases:</b> <tt>" + Arrays.toString(command.getAliases()) + "</tt></div>");
            }
            String role = getRoleName(command.getAccessLevel());
            if (role != null) {
                out.println("<div><b>Access Level:</b> " + role + "</div>");
            }
            out.println();
            out.println();
            System.out.println(command.getUsage(Locale.ENGLISH));
        }
        out.flush();
        out.close();
    }
