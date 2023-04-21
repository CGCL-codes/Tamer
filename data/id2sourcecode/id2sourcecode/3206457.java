    private void copyFile(String resourceName, File dir, String filename) {
        try {
            File dest = new File(dir, filename);
            FileUtils.copyFile(InstanceLauncherFactory.class.getResourceAsStream(resourceName), dest);
        } catch (IOException ioe) {
        }
    }
