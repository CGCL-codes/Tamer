    public boolean openConnection(String drv, String url, String uid, String pwd) {
        InfoDriverPane.InfoElement dinfo = new InfoDriverPane.InfoElement();
        dinfo.driver = drv;
        InfoProfilePane.InfoElement pinfo = new InfoProfilePane.InfoElement(dinfo);
        pinfo.url = url;
        pinfo.uid = uid;
        return openConnection(pinfo, pwd);
    }
