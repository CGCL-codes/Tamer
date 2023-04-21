    public void copyResources(File sourceDirectory, File webappDirectory, String webXml) throws IOException {
        if (!sourceDirectory.equals(webappDirectory)) {
            getLog().info("Copy webapp resources to " + webappDirectory.getAbsolutePath());
            if (getWarSourceDirectory().exists()) {
                String[] fileNames = getWarFiles(sourceDirectory);
                for (int i = 0; i < fileNames.length; i++) {
                    FileUtils.copyFile(new File(sourceDirectory, fileNames[i]), new File(webappDirectory, fileNames[i]));
                }
            }
            if (webXml != null && !"".equals(webXml)) {
                File webinfDir = new File(webappDirectory, WEB_INF);
                FileUtils.copyFile(new File(webXml), new File(webinfDir, "/web.xml"));
            }
        }
    }
