    public synchronized void run() {
        String shellPath = null;
        String shellDirectory = null;
        try {
            shellPath = AcideResourceManager.getInstance().getProperty("consolePanel.shellPath");
            shellDirectory = AcideResourceManager.getInstance().getProperty("consolePanel.shellDirectory");
            File shellPathFile = new File(shellPath);
            File shellDirectoryFile = new File(shellDirectory);
            if ((shellPathFile.exists() && shellPathFile.isFile()) && (shellDirectoryFile.exists() && shellDirectoryFile.isDirectory())) {
                File path = new File(shellDirectory);
                _process = Runtime.getRuntime().exec(shellPath, null, path);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        AcideMainWindow.getInstance().getConsolePanel().getTextPane().setEditable(true);
                    }
                });
                _writer = new BufferedWriter(new OutputStreamWriter(_process.getOutputStream()));
                AcideConsoleInputProcess inputThread = new AcideConsoleInputProcess(_writer, System.in);
                AcideConsoleOutputProcess errorGobbler = new AcideConsoleOutputProcess(_process.getErrorStream(), AcideMainWindow.getInstance().getConsolePanel());
                AcideConsoleOutputProcess outputGobbler = new AcideConsoleOutputProcess(_process.getInputStream(), AcideMainWindow.getInstance().getConsolePanel());
                errorGobbler.start();
                outputGobbler.start();
                inputThread.start();
                try {
                    _process.waitFor();
                } catch (InterruptedException exception) {
                    AcideLog.getLog().error(exception.getMessage());
                    exception.printStackTrace();
                }
            } else {
                setDefaultConfiguration();
            }
        } catch (Exception exception) {
            AcideLog.getLog().error(exception.getMessage());
            JOptionPane.showMessageDialog(AcideMainWindow.getInstance(), AcideLanguageManager.getInstance().getLabels().getString("s1017"), "Error", JOptionPane.ERROR_MESSAGE);
            AcideProjectConfiguration.getInstance().setIsModified(true);
            setDefaultConfiguration();
        }
    }
