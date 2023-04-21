    private static void upgradeTo0511() {
        logger.debug(Messages.getString("Installer.upgradingTo", "0.511"));
        String title = Messages.getString("Installer.upgradeTo") + MindRaider.getVersion() + ":" + Messages.getString("Installer.repositoryBackup");
        String action = "<html>&nbsp;&nbsp;<b>" + Messages.getString("Installer.zipping") + ":</b>&nbsp;&nbsp;</html>";
        ProgressDialogJFrame progress = new ProgressDialogJFrame(title, action);
        backupRepository(progress);
        if (logger.isDebugEnabled()) {
            logger.debug("upgradeTo0511() -      Upgrading documentation... <");
        }
        try {
            String mrFolderUri = MindRaiderVocabulary.getFolderUri(NotebookCustodian.MR_DOC_FOLDER_LOCAL_NAME);
            if (!MindRaider.folderCustodian.exists(mrFolderUri)) {
                logger.debug(Messages.getString("Installer.creatingMindRaiderFolder"));
                MindRaider.folderCustodian.create("MR", mrFolderUri);
            }
            String introNotebookUri = MindRaiderVocabulary.getNotebookUri(NotebookCustodian.MR_DOC_NOTEBOOK_INTRODUCTION_LOCAL_NAME);
            if (!MindRaider.notebookCustodian.exists(introNotebookUri)) {
                logger.debug(Messages.getString("Installer.creatingIntroductionNotebook"));
                MindRaider.notebookCustodian.create("Introduction", introNotebookUri, "MR Introduction", false);
                MindRaider.folderCustodian.addNotebook(mrFolderUri, introNotebookUri);
            }
            String docNotebookUri = MindRaiderVocabulary.getNotebookUri(NotebookCustodian.MR_DOC_NOTEBOOK_DOCUMENTATION_LOCAL_NAME);
            if (!MindRaider.notebookCustodian.exists(docNotebookUri)) {
                logger.debug(Messages.getString("Installer.creatingDocumentationNotebook"));
                MindRaider.notebookCustodian.create("Documentation", docNotebookUri, "MR Documentation", false);
                MindRaider.folderCustodian.addNotebook(mrFolderUri, docNotebookUri);
            }
            String developersNotebookUri = MindRaiderVocabulary.getNotebookUri(NotebookCustodian.MR_DOC_NOTEBOOK_FOR_DEVELOPERS_LOCAL_NAME);
            if (!MindRaider.notebookCustodian.exists(developersNotebookUri)) {
                logger.debug(Messages.getString("Installer.creatingForDevelopersNotebook"));
                MindRaider.notebookCustodian.create("For Developers", developersNotebookUri, "For Developers", false);
                MindRaider.folderCustodian.addNotebook(mrFolderUri, developersNotebookUri);
            }
            upgradeDocumentationNotebook(NotebookCustodian.MR_DOC_NOTEBOOK_INTRODUCTION_LOCAL_NAME);
            upgradeDocumentationNotebook(NotebookCustodian.MR_DOC_NOTEBOOK_DOCUMENTATION_LOCAL_NAME);
            upgradeDocumentationNotebook(NotebookCustodian.MR_DOC_NOTEBOOK_FOR_DEVELOPERS_LOCAL_NAME);
            ExplorerJPanel.getInstance().refresh();
        } catch (Exception e) {
            logger.debug("upgradeTo0511(): unable to upgrade documentation!");
        }
        logger.debug(">\n" + Messages.getString("Installer.documentationUpgraded"));
        try {
            String categoriesOntologySuffix = File.separator + MindRaiderConstants.MR_DIR_CATEGORIES_DIR + File.separator + "notebook.rdf.xml";
            String target = MindRaider.profile.getHomeDirectory() + categoriesOntologySuffix;
            File file = new File(target);
            file.getParentFile().mkdirs();
            FileUtils.copyFile(new File(MindRaider.installationDirectory + DIR_DISTRIBUTION_SKELETON + categoriesOntologySuffix), new File(target));
        } catch (Exception e2) {
            logger.error(Messages.getString("Installer.unableToCopyNotebooksCategoriesOntology"), e2);
            logger.error("upgradeTo0511()", e2);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(Messages.getString("Installer.internationalization") + " <");
        }
        title = Messages.getString("Installer.internationalizationUpgradeTo", MindRaider.getVersion());
        action = "<html>&nbsp;&nbsp;<b>XML file:</b>&nbsp;&nbsp;</html>";
        progress = new ProgressDialogJFrame(title, action);
        try {
            internationalizationUpgradeTo511(new File(MindRaider.profile.getHomeDirectory()), progress);
        } catch (Exception e) {
            logger.debug(Messages.getString("Installer.unableToInternationalize"), e);
        } finally {
            progress.dispose();
            logger.debug(">\n" + Messages.getString("Installer.internationalizationUpgradeFinished"));
        }
        SearchCommander.rebuildSearchAndTagIndices();
        MindRaider.profile.setVersion(MindRaider.getVersion());
        MindRaider.profile.save();
    }
