    @SuppressWarnings("static-access")
    private KeyTab getCopy(KeyTab kt) throws IOException {
        if (kt != null) {
            File srcFile = new File(tmpKeyTab.tabName());
            File destFile = File.createTempFile("keytab", "");
            FileUtils.copyFile(srcFile, destFile);
            KeyTab.refresh();
            return KeyTab.getInstance(destFile);
        }
        return null;
    }
