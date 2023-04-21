    public void setConf(Configuration conf) {
        super.setConf(conf);
        if (conf == null) return;
        if (this.scopedRules == null) {
            String filename = getConf().get("urlnormalizer.regex.file");
            scopedRules = new HashMap();
            URL url = getConf().getResource(filename);
            List rules = null;
            if (url == null) {
                LOG.warn("Can't load the default config file! " + filename);
                rules = EMPTY_RULES;
            } else {
                try {
                    rules = readConfiguration(url.openStream());
                } catch (Exception e) {
                    LOG.warn("Couldn't read default config from '" + url + "': " + e);
                    rules = EMPTY_RULES;
                }
            }
            scopedRules.put(URLNormalizers.SCOPE_DEFAULT, rules);
        }
    }
