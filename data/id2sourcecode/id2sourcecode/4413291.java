    public IEIndexParser(File file) {
        this.file = file;
        try {
            FileInputStream fis = new FileInputStream(file);
            fileChannel = fis.getChannel();
            getBrowserVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
