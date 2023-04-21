    public void testBuildProject() throws Exception {
        ProjectConfigurationReader reader = new ProjectConfigurationReader();
        URL url = getClassRelativeResourceURL("a/mcs-project.xml");
        InputStream stream = url.openStream();
        RuntimeProjectConfiguration configuration;
        try {
            configuration = reader.readProject(stream, url.toExternalForm());
        } finally {
            stream.close();
        }
        final RuntimePolicySourceFactoryMock policySourceFactoryMock = new RuntimePolicySourceFactoryMock("policySourceFactoryMock", expectations);
        final XMLPolicySourceMock xmlPolicySourceMock = new XMLPolicySourceMock("xmlPolicySourceMock", expectations);
        final GroupMock groupMock = new GroupMock("groupMock", expectations);
        policyCacheMock.expects.getLocalDefaultGroup().returns(groupMock);
        urlMapperMock.expects.mapInternalURLToExternalPath(getClassRelativeResourceURLAsString("a/")).returns("/project/a/mcs-project.xml");
        String directory = getClassRelativeResourceURLAsString("a/").substring("file:".length());
        policySourceFactoryMock.expects.createXMLPolicySource(directory).returns(xmlPolicySourceMock);
        RuntimeProjectConfigurator configurator = new RuntimeProjectConfiguratorImpl(localConstraintsMap, remoteConstraintsMap, urlMapperMock, policyCacheMock);
        RuntimeProject project = configurator.buildProject(configuration, null, policySourceFactoryMock);
        assertSame(xmlPolicySourceMock, project.getPolicySource());
        assertFalse(project.isRemote());
        assertSame(localConstraintsMap, project.getCacheControlConstraintsMap());
        assertSame(groupMock, project.getCacheGroup());
    }
