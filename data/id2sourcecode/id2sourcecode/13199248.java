    @Override
    public void start() throws Exception {
        LOG.error("connecting to " + url);
        connection = url.openConnection();
        headerfields = connection.getHeaderFields();
        if (headerfields.containsKey("Content-Type")) {
            List<String> ct = headerfields.get("Content-Type");
            for (int i = 0; i < ct.size(); ++i) {
                String key = ct.get(i);
                int j = key.indexOf("boundary=");
                if (j != -1) {
                    boundryKey = key.substring(j + 9);
                }
            }
        }
        input = connection.getInputStream();
    }
