    protected boolean open() {
        try {
            if (!FileUtil.canRead(resource)) return false;
            URL url = new URL(resource);
            con = (HttpURLConnection) url.openConnection();
            in = con.getInputStream();
        } catch (Exception e) {
            log.error("Can not open URL :" + resource, e);
            return false;
        }
        return true;
    }
