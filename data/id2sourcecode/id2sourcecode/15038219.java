    @Test
    public void testSuccessfulRegistration() throws IOException, ClassNotFoundException {
        url = mockControl.createMock(WevoURL.class);
        connection = mockControl.createMock(HttpURLConnection.class);
        EasyMock.expect(url.openConnection()).andReturn(connection);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.setRequestProperty(DistributionServlet.SLAVE_ID_PROPERTY, "slaveName");
        connection.connect();
        EasyMock.expect(connection.getResponseMessage()).andReturn("ignored");
        EasyMock.expect(connection.getHeaderField(DistributionServlet.SLAVE_ID_PROPERTY)).andReturn("SlaveID");
        connection.disconnect();
        registrator = new SlaveRegistrator(url);
        mockControl.replay();
        registrator.register("slaveName");
        mockControl.verify();
    }
