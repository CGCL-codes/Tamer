        @Override
        public void actionPerformed(ActionEvent e) {
            boolean ok = true;
            buttonCheck.setEnabled(false);
            if (checkMapPath()) {
                mapFile = new File(topLevelDir);
                logFile = new File(logPath);
                if (logFile.isDirectory()) {
                    reportError("Log file is a directory");
                    ok = false;
                } else if (logFile.exists()) {
                    ok = JOptionPane.showConfirmDialog(null, "Log file already exists: do you want to overwrite it?", "File exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
                    if (ok) logFile.delete();
                }
                if (ok) {
                    boolean ignore = ignoreMissingFloors.isSelected();
                    checker = new RunChecks(mapFile, logFile, mapRootDir, mapRootLen, archRoot, mapText, progressText, ignore, version);
                    checker.start();
                }
            }
        }
