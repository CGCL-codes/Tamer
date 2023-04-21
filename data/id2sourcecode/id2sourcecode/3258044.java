    @Override
    public void init(Config config) throws Exception {
        Properties configure = new Properties();
        URL url = HttlViewHandler.class.getResource("/httl.properties");
        log.info("配置路径:{}", url);
        InputStream is = url.openStream();
        try {
            configure.load(is);
            File dir = new File(config.getTemplateFilePath());
            log.info("启动httl模板引擎:[template.directory:{}]", dir.getAbsolutePath());
            configure.put("template.directory", dir.getAbsolutePath());
            engine = Engine.getEngine(configure);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
