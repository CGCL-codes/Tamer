    @Test
    public void testSendingPopulation() throws IOException {
        mockControl.reset();
        EasyMock.expect(urlMock.openConnection()).andStubReturn(connectionMock);
        connectionMock.setDoOutput(true);
        connectionMock.setUseCaches(false);
        connectionMock.setRequestMethod("POST");
        connectionMock.setRequestProperty("Content-Type", "application/octet-stream");
        connectionMock.setRequestProperty(DistributionServlet.SLAVE_ID_PROPERTY, slaveId);
        connectionMock.connect();
        EasyMock.expect(connectionMock.getOutputStream()).andReturn(null);
        resultSerializerMock.serialize(null, returnedResult);
        EasyMock.expect(connectionMock.getResponseMessage()).andReturn("");
        connectionMock.disconnect();
        mockControl.replay();
        exchanger.sendResult(returnedResult, slaveId);
        mockControl.verify();
    }
