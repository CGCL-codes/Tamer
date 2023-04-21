    public void testGetNodes() throws Exception {
        final Depot depot = Depot.create(DEPOT_NAME, new File(getDepotsBase()), getFrameworkInstance().getDepotResourceMgr());
        FileUtils.copyFileStreams(new File("src/test/com/controltier/ctl/common/test-nodes1.properties"), nodesfile);
        assertTrue(nodesfile.exists());
        Nodes nodes = depot.getNodes();
        assertNotNull(nodes);
        assertEquals("nodes was incorrect size", 2, nodes.listNodes().size());
        assertTrue("nodes did not have correct test node1", nodes.hasNode("testnode1"));
        assertTrue("nodes did not have correct test node2", nodes.hasNode("testnode2"));
    }
