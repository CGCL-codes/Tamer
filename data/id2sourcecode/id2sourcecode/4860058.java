    protected void saveProtocol() {
        File tmpProtocol = new File(tasks.get(0).getProtocolFilename());
        FileUtils.copyFile(new File(getPresentationModel().getBean().getTargetDir(), "fe.filelist"), tmpProtocol, getPresentationModel().getBean().getNixUser());
    }
