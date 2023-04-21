    public static void main(String[] args) {
        if (args.length <= 0) {
            showSyntax();
            System.exit(0);
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                if (args[i].equalsIgnoreCase("-world") || args[i].equalsIgnoreCase("-w")) {
                    if (i + 1 < args.length) {
                        worldPath = args[i + 1];
                        i++;
                    } else {
                        System.out.println("No world specified after option -world");
                    }
                } else if (args[i].equalsIgnoreCase("-log") || args[i].equalsIgnoreCase("-l")) {
                    if (i + 1 < args.length) {
                        logPath = args[i + 1];
                        i++;
                    } else {
                        System.out.println("No save file specified after option -log");
                    }
                } else if (args[i].equalsIgnoreCase("-state") || args[i].equalsIgnoreCase("-s")) {
                    if (i + 1 < args.length) {
                        statePath = args[i + 1];
                        i++;
                    } else {
                        System.out.println("No world specified after option -state");
                    }
                } else if (args[i].equalsIgnoreCase("-savedir") || args[i].equalsIgnoreCase("-sd")) {
                    if (i + 1 < args.length) {
                        saveDirPath = args[i + 1];
                        i++;
                    } else {
                        System.out.println("No path to saves directory specified after option -savedir");
                    }
                } else if (args[i].equalsIgnoreCase("-r")) {
                    rebotFriendly = true;
                } else if (args[i].equalsIgnoreCase("-u")) {
                    unstrict = true;
                } else {
                    System.out.println("Unknown option " + args[i]);
                    showSyntax();
                    System.exit(0);
                }
            } else {
                System.out.println("Wrong syntax, expected some option, found " + args[i]);
                showSyntax();
                System.exit(0);
            }
        }
        if (worldPath != null) {
            Vector gameLog = new Vector();
            InputOutputClient io = new CommandLineClient(gameLog, rebotFriendly, unstrict);
            File inputAsFile = new File(worldPath);
            World theWorld = null;
            if (inputAsFile.isFile()) {
                System.out.println("Attempting world location: " + inputAsFile);
                try {
                    theWorld = new World(URLUtils.stringToURL(WorldLoader.goIntoFileIfCompressed(worldPath)), io, false);
                    System.out.println("World generated.\n");
                    gameLog.addElement(inputAsFile.getAbsolutePath());
                } catch (java.io.IOException ioe) {
                    io.write(UIMessages.getInstance().getMessage("clclient.cannot.read.world", "$file", inputAsFile.toString()) + "\n");
                    ioe.printStackTrace();
                    return;
                }
            } else {
                try {
                    System.out.println("Attempting world location: " + worldPath + "/world.xml");
                    theWorld = new World(worldPath + "/world.xml", io, false);
                    System.out.println("World generated.\n");
                    gameLog.addElement(worldPath + "/world.xml");
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (saveDirPath != null) {
                Paths.setSaveDir(saveDirPath);
            }
            if (statePath != null) {
                try {
                    theWorld.loadState(statePath);
                } catch (Exception exc) {
                    io.write(UIMessages.getInstance().getMessage("clclient.cannot.read.state", "$file", statePath) + "\n");
                    io.write(exc.toString());
                    exc.printStackTrace();
                }
            }
            if (logPath != null) {
                try {
                    theWorld.prepareLog(logPath);
                    theWorld.setRandomNumberSeed(logPath);
                } catch (Exception exc) {
                    io.write(UIMessages.getInstance().getMessage("clclient.cannot.read.log", "$file", logPath) + "\n");
                    exc.printStackTrace();
                    return;
                }
            } else {
                theWorld.setRandomNumberSeed();
            }
            gameLog.addElement(String.valueOf(theWorld.getRandomNumberSeed()));
            GameEngineThread maquinaEstados = new GameEngineThread(theWorld, null, false);
            maquinaEstados.start();
        } else {
            System.out.println(UIMessages.getInstance().getMessage("clclient.world.not.specified"));
        }
    }
