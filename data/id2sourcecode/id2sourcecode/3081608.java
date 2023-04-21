    @Override
    protected void doStore() {
        try {
            File dir = new File(this.getStorePath(""));
            dir.mkdirs();
            for (String name : this.namePathMap.keySet()) {
                File from = new File(this.namePathMap.get(name));
                File to = new File(this.getStorePath(name));
                FileUtils.copyFile(from, to);
            }
        } catch (IOException e) {
            Activator.showErrorDialog(ResourceString.getResourceString("error.read.file"));
        }
        super.doStore();
    }
