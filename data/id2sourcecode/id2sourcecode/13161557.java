    public void testGetNodes() throws Exception {
        final FrameworkProject project = FrameworkProject.create(PROJECT_NAME, new File(getFrameworkProjectsBase()), getFrameworkInstance().getFrameworkProjectMgr());
        FileUtils.copyFileStreams(new File("src/test/com/controltier/ctl/common/test-nodes1.xml"), nodesfile);
        assertTrue(nodesfile.exists());
        Nodes nodes = project.getNodes();
        assertNotNull(nodes);
        assertEquals("nodes was incorrect size", 2, nodes.listNodes().size());
        assertTrue("nodes did not have correct test node1", nodes.hasNode("testnode1"));
        assertTrue("nodes did not have correct test node2", nodes.hasNode("testnode2"));
    }
