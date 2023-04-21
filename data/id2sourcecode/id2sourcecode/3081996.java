    public void execute(String commandName, final ConsoleInput ci, CommandLine commandLine) {
        Appender con = Logger.getRootLogger().getAppender("ConsoleAppender");
        List args = commandLine.getArgList();
        if ((con != null) && (!args.isEmpty())) {
            String subcommand = (String) args.get(0);
            if ("off".equalsIgnoreCase(subcommand)) {
                if (args.size() == 1) {
                    con.addFilter(new DenyAllFilter());
                    ci.out.println("> Console logging off");
                } else {
                    String name = (String) args.get(1);
                    Object[] entry = (Object[]) channel_listener_map.remove(name);
                    if (entry == null) {
                        ci.out.println("> Channel '" + name + "' not being logged");
                    } else {
                        ((LoggerChannel) entry[0]).removeListener((LoggerChannelListener) entry[1]);
                        ci.out.println("> Channel '" + name + "' logging off");
                    }
                }
            } else if ("on".equalsIgnoreCase(subcommand)) {
                if (args.size() == 1) {
                    if (commandLine.hasOption('f')) {
                        String filename = commandLine.getOptionValue('f');
                        try {
                            Appender newAppender = new FileAppender(new PatternLayout("%d{ISO8601} %c{1}-%p: %m%n"), filename, true);
                            newAppender.setName("ConsoleAppender");
                            Logger.getRootLogger().removeAppender(con);
                            Logger.getRootLogger().addAppender(newAppender);
                            ci.out.println("> Logging to filename: " + filename);
                        } catch (IOException e) {
                            ci.out.println("> Unable to log to file: " + filename + ": " + e);
                        }
                    } else {
                        if (!(con instanceof ConsoleAppender)) {
                            Logger.getRootLogger().removeAppender(con);
                            con = new ConsoleAppender(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN));
                            con.setName("ConsoleAppender");
                            Logger.getRootLogger().addAppender(con);
                        }
                        ci.out.println("> Console logging on");
                    }
                    con.clearFilters();
                } else {
                    Map channel_map = getChannelMap(ci);
                    final String name = (String) args.get(1);
                    LoggerChannel channel = (LoggerChannel) channel_map.get(name);
                    if (channel == null) {
                        ci.out.println("> Channel '" + name + "' not found");
                    } else if (channel_listener_map.get(name) != null) {
                        ci.out.println("> Channel '" + name + "' already being logged");
                    } else {
                        LoggerChannelListener l = new LoggerChannelListener() {

                            public void messageLogged(int type, String content) {
                                ci.out.println("[" + name + "] " + content);
                            }

                            public void messageLogged(String str, Throwable error) {
                                ci.out.println("[" + name + "] " + str);
                                error.printStackTrace(ci.out);
                            }
                        };
                        channel.addListener(l);
                        channel_listener_map.put(name, new Object[] { channel, l });
                        ci.out.println("> Channel '" + name + "' on");
                    }
                }
            } else if (subcommand.equalsIgnoreCase("list")) {
                Map channel_map = getChannelMap(ci);
                Iterator it = channel_map.keySet().iterator();
                while (it.hasNext()) {
                    String name = (String) it.next();
                    ci.out.println("  " + name + " [" + (channel_listener_map.get(name) == null ? "off" : "on") + "]");
                }
            } else {
                ci.out.println("> Command 'log': Subcommand '" + subcommand + "' unknown.");
            }
        } else {
            ci.out.println("> Console logger not found or missing subcommand for 'log'\r\n> log syntax: log [-f filename] (on [name]|off [name]|list)");
        }
    }
