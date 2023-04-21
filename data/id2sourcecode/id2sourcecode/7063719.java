    public final void addFile() {
        try {
            String fileName = Util.getFileName(window != null ? window.getComponent() : null, null, null);
            File srcFile = new File(fileName);
            FileUtils.copyFileToDirectory(srcFile, attachmentDir);
            fileNameList.add(srcFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
            Util.showErrorDialog(window, "Feil", e.getMessage());
        }
    }
