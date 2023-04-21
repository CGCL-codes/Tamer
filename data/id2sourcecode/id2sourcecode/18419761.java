    @Override
    public void initialize(WikiEngine engine, Properties properties) throws FilterException {
        super.initialize(engine, properties);
        m_engine = engine;
        initKnowWEEnvironmentIfNeeded(engine);
        ResourceBundle knowweconfig = ResourceBundle.getBundle("KnowWE_config");
        if (knowweconfig.getString("knowweplugin.jspwikiconnector.copycorepages").equals("false")) {
            WikiEventUtils.addWikiEventListener(engine.getPageManager(), WikiPageEvent.PAGE_DELETE_REQUEST, this);
            return;
        }
        File f = new File(KnowWEEnvironment.getInstance().getKnowWEExtensionPath());
        f = f.getParentFile();
        try {
            for (File s : f.listFiles()) {
                if (s.getName().equals("WEB-INF")) {
                    BufferedReader in = new BufferedReader(new FileReader(s.getPath() + "/jspwiki.properties"));
                    String zeile = null;
                    File pagedir = null;
                    while ((zeile = in.readLine()) != null) {
                        if (!zeile.contains("#") && zeile.contains("jspwiki.fileSystemProvider.pageDir")) {
                            zeile = zeile.trim();
                            zeile = zeile.substring(zeile.lastIndexOf(" ") + 1);
                            pagedir = new File(zeile);
                            in.close();
                            break;
                        }
                    }
                    if (pagedir.exists()) {
                        File[] files = pagedir.listFiles();
                        File coreDir = new File(s.getPath() + "/resources/core-pages");
                        File[] cores = coreDir.listFiles();
                        for (File cP : coreDir.listFiles()) {
                            if (!cP.getName().endsWith(".txt")) continue;
                            File newFile = new File(pagedir.getPath() + "/" + cP.getName());
                            if (!newFile.exists()) FileUtils.copyFile(cP, newFile);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        WikiEventUtils.addWikiEventListener(engine.getPageManager(), WikiPageEvent.PAGE_DELETE_REQUEST, this);
        WikiEventUtils.addWikiEventListener(engine.getPageManager(), WikiPageRenameEvent.PAGE_RENAMED, this);
    }
