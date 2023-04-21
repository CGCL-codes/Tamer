    public FrameworkProject createFrameworkProject() {
        final File projectDir = new File(getFrameworkProjectsBase(), PROJECT);
        FrameworkProject project = null;
        try {
            FrameworkProject.createFileStructure(projectDir, true);
            final CtlProjectMain setup = new CtlProjectMain();
            final String[] args = new String[] { "-p", PROJECT, "-b", "src/ant/controllers/ctl/projectsetupCmd.xml", "-o", "-v" };
            setup.parseArgs(args);
            setup.executeAction();
            final File templateBasedir = new File("src/templates/ant");
            final File modulesDir = new File(projectDir, "modules");
            assertTrue("project modules lib dir does not exist", modulesDir.exists());
            final CtlGenMain creator = CtlGenMain.create(templateBasedir, modulesDir);
            project = getFrameworkInstance().getFrameworkProjectMgr().createFrameworkProject(PROJECT);
            final FrameworkType mType = project.createType(MODULE_NAME);
            File destNodesFile = new File(project.getEtcDir(), "resources.xml");
            File testNodesFile = new File(TestNodeDispatchAction.TEST_NODES_XML);
            try {
                FileUtils.copyFileStreams(testNodesFile, destNodesFile);
            } catch (IOException e) {
                fail(e.getMessage());
            }
        } catch (Throwable t) {
            fail(t.getMessage());
        }
        return project;
    }
