    public static void uploadPromptFile(Prompt prompt, File f) throws Exception {
        if (!f.exists()) {
            throw new FileNotFoundException("Couldn't find actionpak file " + f);
        }
        Date lm = new Date(f.lastModified());
        String promptName = prompt.getName();
        String pid = prompt.getProject() == null ? null : prompt.getProject().getName();
        if (prompt.getExtension() != null) promptName += "." + prompt.getExtension();
        if (pid != null) promptName = pid + "/" + promptName;
        String promptFileName = prompt.getName() + '.' + prompt.getExtension();
        boolean promptNeedsUpdate = SafiServerRemoteManager.getInstance().promptNeedsUpdate(pid, promptFileName, lm);
        if (!promptNeedsUpdate) {
            promptNeedsUpdate = MessageDialog.openConfirm(getActiveShell(), "Newer File Exists", "A newer prompt file by this name already exists on the server.  Do you still want to overwrite?");
        }
        if (promptNeedsUpdate) {
            byte[] data = new byte[(int) f.length()];
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(data);
            bis.close();
            SafiServerRemoteManager.getInstance().transferPrompt(pid, promptFileName, data);
            promptCache.remove(prompt.getId());
        }
    }
