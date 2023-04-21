    public String userDownload() {
        this.download = downloadMng.findById(id);
        if (download.getAttachment() == null) {
            addActionMessage("����������ϲ����ڻ�������ʧ");
            chnl = download.getChannel();
            sysType = chnl.getSysType();
            tplPath = download.chooseTpl();
            return FAIL;
        }
        download(download);
        download.setDownCount(download.getDownCount() + 1);
        downloadMng.update(download);
        return null;
    }
