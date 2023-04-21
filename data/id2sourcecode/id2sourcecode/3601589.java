    @Test
    @Ignore
    public void dwOpenConnectionURL() throws IOException {
        URL url = new URL(DNS_URL);
        InputStream is = url.openConnection().getInputStream();
        Assert.assertNotNull("input stream is null", is);
        ModelImport mi = CastorUtils.unmarshalWithTranslatedExceptions(ModelImport.class, is);
        Assert.assertTrue("Number of nodes in Model Import > 1", 1 == mi.getNodeCount());
        Assert.assertTrue("NodeLabel isn't localhost", "localhost".equals(mi.getNode(0).getNodeLabel()));
        Assert.assertTrue("127.0.0.1".equals(mi.getNode(0).getInterface(0).getIpAddr()));
    }
