    private void setUpCommonGettingBehavior() throws IOException {
        EasyMock.expect(urlMock.openConnection()).andStubReturn(connectionMock);
        connectionMock.setDoInput(true);
        connectionMock.setUseCaches(false);
        connectionMock.setRequestMethod("GET");
        connectionMock.setRequestProperty(DistributionServlet.SLAVE_ID_PROPERTY, slaveId);
        connectionMock.connect();
        connectionMock.disconnect();
    }
