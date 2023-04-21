    public void open(String path) throws IOException {
        super.open(path);
        path = path.replaceAll("\\\\", "\\\\\\\\");
        if (mplayerProcess != null) close();
        if (mplayerProcess == null) {
            String command = "\"" + mplayerPath + "\" " + mplayerOptions + " \"" + path + "\"";
            logger.info("Starting MPlayer process: " + command);
            mplayerProcess = Runtime.getRuntime().exec(command);
            mplayerDetector = new net.sf.smartcrib.media.ProcessExitDetector(mplayerProcess);
            mplayerDetector.addProcessListener(new ProcessListener() {

                public void processFinished(Process process) {
                    mplayerProcess = null;
                    playing = false;
                    firePlayerItemEnded();
                }
            });
            mplayerDetector.start();
            PipedInputStream readFrom = new PipedInputStream(1024 * 1024);
            PipedOutputStream writeTo = new PipedOutputStream(readFrom);
            mplayerOutErr = new BufferedReader(new InputStreamReader(readFrom));
            new LineRedirecter(mplayerProcess.getInputStream(), writeTo, "MPlayer says: ").start();
            new LineRedirecter(mplayerProcess.getErrorStream(), writeTo, "MPlayer encountered an error: ").start();
            mplayerIn = new PrintStream(mplayerProcess.getOutputStream());
        }
        waitForAnswer("Starting playback...");
        playing = true;
        firePlayerStarted();
        logger.info("Started playing file " + path);
    }
