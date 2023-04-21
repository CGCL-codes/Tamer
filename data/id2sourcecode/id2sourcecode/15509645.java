    public void testGenerateAllLanguages() {
        String path = "Gallery/Gallery.xml";
        File file = new File(path);
        if (file.exists() && generate) {
            String[] languages = null;
            try {
                languages = LanguageVariantFactory.getVariantNames();
                Settings.load(MainApplication.FILE_PROPERTIES);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            for (int i = 0; i < languages.length; i++) {
                Settings.setProperty("language.variant", languages[i]);
                MainApplicationModel model = MainApplicationModel.getInstance();
                model.setCurrentDirectory(file);
                KendoProject project = model.getActiveProject();
                project.setFile(file);
                DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.LOCAL_PERSISTENCE);
                ProjectDAO dao = factory.getProjectDAO(project);
                dao.open();
                Settings.setProperty("language.variant", languages[i]);
                KendoModel m = project.getModelByName("Gallery");
                String tempFile = file.getParent() + File.separator + m.getName() + "-" + languages[i] + ".rtf";
                File rtfFile = new File(tempFile);
                Composer composer = new Composer(null, project, m, rtfFile);
                Thread writeThread = new Thread(composer);
                writeThread.start();
            }
        }
    }
