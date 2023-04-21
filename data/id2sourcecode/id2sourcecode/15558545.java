    public PartidaEnCurso initPartidaDedicada(PartidaEntry pe, ServerLogWindow slw, String stateFile, String logFile) {
        Debug.println(pe);
        Debug.println(pe.getGameInfo());
        Debug.println(pe.getGameInfo().getFile());
        String ficheroMundo = pe.getGameInfo().getFile();
        World theWorld;
        InputOutputClient worldIO = slw.addTab();
        Vector gameLog = new Vector();
        try {
            theWorld = new World(ficheroMundo, worldIO, true);
            gameLog.addElement(ficheroMundo);
        } catch (Exception e) {
            worldIO.write("Excepci�n al crear el mundo: " + e + "\n");
            e.printStackTrace();
            return null;
        }
        if (stateFile != null) {
            try {
                theWorld.loadState(stateFile);
            } catch (Exception exc) {
                worldIO.write(UIMessages.getInstance().getMessage("swing.cannot.read.state", "$file", stateFile));
                worldIO.write(exc.toString());
                exc.printStackTrace();
            }
        }
        if (logFile != null) {
            try {
                Debug.println("SHPL");
                Debug.println("Player list is " + theWorld.getPlayerList());
                theWorld.prepareLog(logFile);
                theWorld.setRandomNumberSeed(logFile);
            } catch (Exception exc) {
                worldIO.write(UIMessages.getInstance().getMessage("swing.cannot.read.log", "$exc", exc.toString()));
                return null;
            }
        } else {
            theWorld.setRandomNumberSeed();
        }
        gameLog.addElement(String.valueOf(theWorld.getRandomNumberSeed()));
        GameEngineThread maquinaEstados = new GameEngineThread(theWorld, slw, true);
        maquinaEstados.start();
        return new PartidaEnCurso(theWorld, pe.getMaxPlayers(), pe.getName(), pe.getPassword());
    }
