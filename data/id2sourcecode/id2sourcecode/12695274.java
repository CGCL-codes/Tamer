        public void runThread() {
            if (!LibLoader.isRunning()) {
                try {
                    LibLoader.startProcess();
                } catch (IOException ioe) {
                    Log.war("Cannot run libLoader", Log.WARNING);
                    ioe.printStackTrace(System.err);
                    return;
                }
            }
            if (libraryToLoad != null) {
                net.sourceforge.sevents.scripthandle.Sevents.getProcess().setTeamId(id);
                try {
                    LibLoader.loadLibrary(libraryToLoad);
                    LibLoader.setFunction("run", null);
                    LibLoader.runThread(SSettings.getInt(FileUtils.writeMaxProcDelay, FileUtils.defaultMaxProcDelay));
                    if (LibLoader.isRunning()) {
                        LibLoader.unloadLibrary();
                    }
                } catch (IOException ioe) {
                    Log.war("runThread2: " + ioe.getClass().getName() + " " + ioe.getMessage(), Log.WARNING);
                }
                net.sourceforge.sevents.scripthandle.Sevents.getProcess().setTeamId("");
            }
        }
