    public String setIp(String md5) {
        String ret = null;
        try {
            URL url = new URL(config.getServerPhp() + "/" + getKey + "?op=write&md5=" + md5 + "&ip=" + this.getMyIp() + ":" + config.getPort());
            ret = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
            debug.print("[ OK ] Sign on php server " + config.getServerPhp());
        } catch (IOException e) {
            debug.print("[ ERROR ] Sign on php server " + config.getServerPhp());
        }
        return ret;
    }
