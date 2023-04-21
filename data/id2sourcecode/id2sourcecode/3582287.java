    public WindowsInstallerExec(String productGuid, File javaHome, File installDir, boolean startMenuShortcutsDisabled, boolean desktopShortcutsDisabled, Collection notAssociatedExtensions) throws IOException {
        this.tempFile.deleteOnExit();
        this.errorLogFile.deleteOnExit();
        this.productGuid = productGuid;
        this.javaHome = javaHome;
        this.installDir = installDir;
        this.startMenuShortcutsDisabled = startMenuShortcutsDisabled;
        this.desktopShortcutsDisabled = desktopShortcutsDisabled;
        this.notAssociatedExtensions = notAssociatedExtensions;
        FileOutputStream fos = new FileOutputStream(tempFile);
        InputStream fileStream = WindowsInstallerExec.class.getResourceAsStream("/windows-installer.msi");
        byte buffer[] = new byte[200 * 1024];
        int length;
        while ((length = fileStream.read(buffer)) != -1) fos.write(buffer, 0, length);
        fileStream.close();
        fos.close();
    }
