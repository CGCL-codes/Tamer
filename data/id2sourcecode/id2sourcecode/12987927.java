    private void initializeDefaults() {
        String executable = "ShellContextMenu.exe";
        String params = " 10000 {0}";
        String shell = "";
        if (!System.getProperty("os.name").startsWith("Windows")) {
            executable = "scm";
            params = " {0}";
            shell = "bash ";
        }
        String metadata = getStateLocation().toOSString();
        String file = metadata + System.getProperty("file.separator") + executable;
        String executableWithParams = shell + file + params;
        IPreferenceStore store = getPreferenceStore();
        store.setDefault(ContextMenuPreferencePage.P_TARGET, executableWithParams);
        File targetFile = new File(file);
        if (targetFile.exists()) {
            return;
        }
        try {
            URL url = getDefault().getBundle().getEntry(executable);
            InputStream in = url.openConnection().getInputStream();
            FileOutputStream out = new FileOutputStream(targetFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
        } catch (IOException e) {
            String msg = "Unable to find " + file + ":\n" + "Please enter manually in Window->Preferences->Context Menu";
            logError(msg, e);
            MessageDialog.openInformation(new Shell(), "ContextMenuPlugin", msg);
        }
    }
