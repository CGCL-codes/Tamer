    public void testDoAction() throws Exception {
        Framework fw = getFrameworkInstance();
        NodeSet nodeset = new NodeSet();
        {
            IDispatchedScript dsc = new DispatchedScriptImpl(nodeset, TEST_PROJ, null, null, null, new String[] { "id" }, 0);
            CommandAction action = new CommandAction(fw, dsc, null);
            final NodeEntryImpl nodeentry = new NodeEntryImpl(CtlTest.localNodeHostname, CtlTest.localNodeHostname);
            final boolean wascalled[] = { false };
            action.setNodeDispatcher(new NodeDispatcher() {

                public void executeNodedispatch(Project project, Collection<INodeEntry> nodes, int threadcount, boolean keepgoing, FailedNodesListener failedListener, NodeCallableFactory factory) {
                    wascalled[0] = true;
                }
            });
            try {
                action.doAction();
                fail("should not succeed.");
            } catch (Exception e) {
                assertTrue(e instanceof NodesetEmptyException);
            }
            assertFalse(wascalled[0]);
        }
        File destNodesFile = new File(project.getEtcDir(), "resources.xml");
        File testNodesFile = new File(TEST_NODES_XML);
        try {
            FileUtils.copyFileStreams(testNodesFile, destNodesFile);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        {
            IDispatchedScript dsc = new DispatchedScriptImpl(nodeset, TEST_PROJ, null, null, null, new String[] { "id" }, 0);
            CommandAction action = new CommandAction(fw, dsc, null);
            final NodeEntryImpl nodeentry = new NodeEntryImpl(CtlTest.localNodeHostname, CtlTest.localNodeHostname);
            final boolean wascalled[] = { false };
            action.setNodeDispatcher(new NodeDispatcher() {

                public void executeNodedispatch(Project project, Collection<INodeEntry> nodes, int threadcount, boolean keepgoing, FailedNodesListener failedListener, NodeCallableFactory factory) {
                    wascalled[0] = true;
                }
            });
            action.doAction();
            assertTrue(wascalled[0]);
        }
    }
