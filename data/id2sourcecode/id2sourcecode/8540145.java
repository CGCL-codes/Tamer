    @Override
    public void initialize(WikiEngine engine, Properties properties) throws FilterException {
        super.initialize(engine, properties);
        m_engine = engine;
        initEnvironmentIfNeeded(engine);
        ResourceBundle knowweconfig = KnowWEUtils.getConfigBundle();
        if (knowweconfig.getString("knowweplugin.jspwikiconnector.copycorepages").equals("false")) {
            WikiEventUtils.addWikiEventListener(engine.getPageManager(), WikiPageEvent.PAGE_DELETE_REQUEST, this);
            return;
        }
        try {
            BufferedReader in = new BufferedReader(new FileReader(KnowWEUtils.getApplicationRootPath() + "/WEB-INF/jspwiki.properties"));
            String line = null;
            File pagedir = null;
            while ((line = in.readLine()) != null) {
                if (!line.contains("#") && line.contains("jspwiki.fileSystemProvider.pageDir")) {
                    line = line.trim();
                    line = line.substring(line.lastIndexOf(" ") + 1);
                    pagedir = new File(line);
                    in.close();
                    break;
                }
            }
            if (pagedir.exists()) {
                File coreDir = new File(KnowWEUtils.getApplicationRootPath() + "/WEB-INF/resources/core-pages");
                for (File corePage : coreDir.listFiles()) {
                    if (!corePage.getName().endsWith(".txt")) continue;
                    File newFile = new File(pagedir.getPath() + "/" + corePage.getName());
                    if (!newFile.exists()) FileUtils.copyFile(corePage, newFile);
                }
            }
        } catch (Exception e) {
        }
        WikiEventUtils.addWikiEventListener(engine.getPageManager(), WikiPageEvent.PAGE_DELETE_REQUEST, this);
        WikiEventUtils.addWikiEventListener(engine.getPageManager(), WikiPageRenameEvent.PAGE_RENAMED, this);
    }
