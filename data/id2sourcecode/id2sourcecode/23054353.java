    public boolean load() {
        if ((config_file == null) && (config_url == null)) return false;
        if (config_url != null) {
            try {
                return load(new BufferedReader(new InputStreamReader(config_url.openStream())));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            if (!config_file.exists()) return false;
            try {
                return load(new BufferedReader(new FileReader(config_file)));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
