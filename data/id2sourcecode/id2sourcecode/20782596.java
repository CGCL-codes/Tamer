    private void downcontent(String chnlName, Long id) {
        download = downloadMng.findById(id);
        chnl = download.getChannel();
        download.setVisitTotal(download.getVisitTotal() + 1);
        sysType = chnl.getSysType();
        tplPath = download.chooseTpl();
    }
