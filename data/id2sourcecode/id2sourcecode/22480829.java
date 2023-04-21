    protected boolean writeTemporal() {
        boolean r = true;
        Synchronizable sync = getData();
        if (sync != null) {
            if (saveLocalSync(sync)) {
                setData(null);
                ProxyUtil.log("UploadSyncThread::writeTemporal(): file " + m_sTempPath + " has been written");
            } else {
                r = false;
            }
        }
        return r;
    }
