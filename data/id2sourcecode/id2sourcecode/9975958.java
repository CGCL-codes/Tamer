    public String getKey(String md5) throws IOException {
        URL url = new URL(config.getServerPhp() + "/" + getKey + "?op=read&name=key&md5=" + md5);
        String key = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
        debug.print("SL Object key: " + key);
        return key;
    }
