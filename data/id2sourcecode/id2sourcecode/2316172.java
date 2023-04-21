    public static void main(String[] args) {
        progress.setVisible(true);
        String step = null;
        try {
            if (checkJavaVersion()) {
                if (lumberjackAlreadyInstalled()) {
                    int answer = JOptionPane.showConfirmDialog(progress, "Lumberjack is already installed. Overwrite?", "Overwrite current installation?", JOptionPane.OK_CANCEL_OPTION);
                    if (answer != JOptionPane.OK_OPTION) {
                        JOptionPane.showMessageDialog(progress, "Installation cancelled...");
                        System.exit(1);
                    }
                    progress.addMessage("Overwriting existing installation...");
                    progress.mark(progress.CHECK_EXISTING);
                    try {
                        removeExistingLumberjack();
                    } catch (Exception e) {
                        step = "Problems removing existing Lumberjack";
                        throw e;
                    }
                } else {
                    progress.mark(progress.CHECK_EXISTING);
                }
                try {
                    installClassFiles();
                    progress.mark(progress.INSTALL_CLASSES);
                } catch (Exception e) {
                    step = "Problems installing class files.";
                    throw e;
                }
                boolean installProperties = true;
                if (loggingPropertiesAlreadyInstalled()) {
                    int answer = JOptionPane.showConfirmDialog(progress, "'logging.properties' exists. Overwrite?", "Overwrite current logging.properties?", JOptionPane.OK_CANCEL_OPTION);
                    if (answer != JOptionPane.OK_OPTION) {
                        progress.addMessage("Not installing 'logging.properties'...");
                        installProperties = false;
                    } else {
                        progress.addMessage("Overwriting existing 'logging.properties'...");
                    }
                }
                if (installProperties) {
                    try {
                        installPropertiesFile();
                        progress.mark(progress.INSTALL_PROPERTIES);
                    } catch (Exception e) {
                        step = "Problems installing logging.properties.";
                        throw e;
                    }
                }
                progress.enableClose("All done!", 0);
            }
        } catch (Exception e) {
            progress.addMessage(step);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            progress.enableClose(sw.getBuffer().toString(), 1);
        }
    }
