    protected void copyResourceToStage(File resource, String folder) throws Exception {
        if (folder != null) {
            folder = folder + File.separator;
        } else {
            folder = "";
        }
        String name = resource.getName();
        File target = new File(stageDirectory + File.separator + folder + name);
        if (resource.lastModified() > target.lastModified()) {
            FileUtils.copyFile(resource, target, true);
        }
    }
