    private void createPhaseKnowledgeBase(IFolder factsFolder) {
        try {
            IProject project = factsFolder.getProject();
            IFolder factsdepot = project.getFolder("Facts Depot");
            IFile knowledgeBase = factsdepot.getFile("KnowledgeBase.rub");
            System.out.println(knowledgeBase.isAccessible());
            File sourceFile = new File(knowledgeBase.getLocationURI());
            URI destiniFolderURI = factsFolder.getLocationURI();
            File destiniFile = new File(destiniFolderURI.getPath() + "/KnowledgeBase.rub");
            FileUtils.copyFile(sourceFile, destiniFile);
            String knowledgeBaseFileName = phaseName + ".rub";
            String phaseKnowledgeBaseFilePath = factsFolder.getLocation().toString() + String.valueOf(Path.SEPARATOR) + knowledgeBaseFileName;
            File phaseKnowledgeBaseFile = new File(phaseKnowledgeBaseFilePath);
            FileUtils.touch(phaseKnowledgeBaseFile);
            factsFolder.refreshLocal(IFolder.DEPTH_INFINITE, null);
            config.setProperty("newPhase.knowledgeBaseFile", knowledgeBaseFileName);
            config.save();
        } catch (IOException e) {
            logger.error(e.getClass().getName(), e);
        } catch (CoreException e) {
            logger.error(e.getClass().getName(), e);
        } catch (ConfigurationException e) {
            logger.error(e.getClass().getName(), e);
        }
    }
