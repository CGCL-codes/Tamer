    void doLoad(String resName, URL url, boolean requireExists) throws IOException {
        if (null == url && requireExists) {
            throw new FileNotFoundException("Resource " + resName + " does not exist");
        } else if (null == url) {
            LOG.warn("Resource {} does not exist", resName);
        } else {
            Reader rdr = null;
            try {
                rdr = new InputStreamReader(url.openStream());
                super.load(rdr);
            } finally {
                try {
                    rdr.close();
                } catch (Exception e) {
                }
            }
        }
    }
