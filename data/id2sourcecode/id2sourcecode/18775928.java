    public void testSubmit() throws Exception {
        HttpFetcher fetcher = mock(HttpFetcher.class);
        when(fetcher.execute(any(HttpMessage.class), anyMapOf(String.class, Object.class))).thenReturn(new HttpResponse("POST", new URL("http://foo.google.com"), 0, new ByteArrayInputStream("[{\"id\":\"op1\",\"data\":{}}]".getBytes())));
        MockRobot robot = new MockRobot();
        robot.setupOAuth("consumerKey", "consumerSecret", "http://gmodules.com/api/rpc");
        WaveService service = new WaveService(fetcher, robot.computeHash());
        service.setupOAuth("consumerKey", "consumerSecret", "http://gmodules.com/api/rpc");
        OperationQueue opQueue = new OperationQueue();
        opQueue.appendOperation(OperationType.ROBOT_NOTIFY, Parameter.of(ParamsProperty.CAPABILITIES_HASH, "123"));
        Wavelet wavelet = mock(Wavelet.class);
        when(wavelet.getOperationQueue()).thenReturn(opQueue);
        assertEquals(1, opQueue.getPendingOperations().size());
        robot.submit(wavelet, "http://gmodules.com/api/rpc", service);
        assertEquals(0, opQueue.getPendingOperations().size());
        verify(fetcher, times(1)).execute(any(HttpMessage.class), anyMapOf(String.class, Object.class));
    }
