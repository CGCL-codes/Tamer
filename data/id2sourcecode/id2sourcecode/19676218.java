    public String uploadZip() {
        if (SessionManagement.isValid()) {
            try {
                final String username = SessionManagement.getUsername();
                final Application application = FacesContext.getCurrentInstance().getApplication();
                final ValueBinding vb = application.createValueBinding("#{inputFile}");
                final InputFileBean inputFile = (InputFileBean) vb.getValue(FacesContext.getCurrentInstance());
                File file = inputFile.getFile();
                log.debug("file = " + file);
                final String zipLanguage = inputFile.getLanguage();
                MenuDAO menuDao = (MenuDAO) Context.getInstance().getBean(MenuDAO.class);
                final Menu parent = menuDao.findByPrimaryKey(selectedDirectory);
                log.debug("parent.getMenuId() = " + parent.getMenuId());
                log.debug("parent.getMenuPath() = " + parent.getMenuPath());
                SettingsConfig conf = (SettingsConfig) Context.getInstance().getBean(SettingsConfig.class);
                String path = conf.getValue("userdir");
                if (!path.endsWith(File.pathSeparator)) {
                    path += File.pathSeparator;
                }
                path += username + File.pathSeparator + File.separator;
                log.debug("path = " + path);
                FileUtils.forceMkdir(new File(path));
                final File destFile = new File(path, file.getName());
                log.debug("destFile = " + destFile);
                FileUtils.copyFile(file, destFile);
                inputFile.deleteUploadDir();
                Thread zipImporter = new Thread(new Runnable() {

                    public void run() {
                        ZipImport importer = new ZipImport();
                        importer.setExtractKeywords(inputFile.isExtractKeywords());
                        log.debug("importing: = " + destFile.getPath());
                        importer.process(destFile.getPath(), zipLanguage, parent, username);
                        try {
                            FileUtils.forceDelete(destFile);
                        } catch (IOException e) {
                            log.error("Unable to delete " + destFile, e);
                        }
                    }
                });
                zipImporter.start();
                Messages.addLocalizedInfo("msg.action.importfolder");
                ValueBinding vb2 = application.createValueBinding("#{documentNavigation}");
                DocumentNavigation documentNavigation = (DocumentNavigation) vb2.getValue(FacesContext.getCurrentInstance());
                documentNavigation.refresh();
                documentNavigation.setSelectedPanel(new PageContentBean("documents"));
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                Messages.addError(e.getMessage());
            }
        } else {
            return "login";
        }
        return null;
    }
