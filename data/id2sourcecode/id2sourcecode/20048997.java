    @Test
    public void shouldCreateTheAppropriateChannelsForAStreamService() throws Exception {
        final LoopbackDevice device = new LoopbackDevice(182);
        UOSMessageContext msgCtx = new UOSMessageContext() {

            public NetworkDevice getCallerDevice() {
                return device;
            }
        };
        ConnectionManagerControlCenter mockNet = mock(ConnectionManagerControlCenter.class);
        ClientConnection cc = mock(ClientConnection.class);
        when(cc.getDataInputStream()).thenReturn(new DataInputStream(null));
        when(cc.getDataOutputStream()).thenReturn(new DataOutputStream(null));
        when(mockNet.openActiveConnection(anyString(), anyString())).thenReturn(cc);
        when(mockNet.getHost(anyString())).thenReturn("myname");
        caller = new ReflectionServiceCaller(mockNet);
        ServiceCall call = new ServiceCall(null, "myService");
        call.setServiceType(ServiceType.STREAM);
        call.setChannelType(device.getNetworkDeviceType());
        call.setChannels(4);
        call.setChannelIDs(new String[] { "P1", "P2", "P3", "P4" });
        caller.callServiceOnDriver(call, new DriverSpy(), msgCtx);
        verify(mockNet).openActiveConnection("myname:P1", call.getChannelType());
        verify(mockNet).openActiveConnection("myname:P2", call.getChannelType());
        verify(mockNet).openActiveConnection("myname:P3", call.getChannelType());
        verify(mockNet).openActiveConnection("myname:P4", call.getChannelType());
        for (int i = 0; i < 4; i++) {
            assertNotNull("InputStream: " + i, msgCtx.getDataInputStream(i));
            assertNotNull("OutputStream: " + i, msgCtx.getDataOutputStream(i));
        }
        assertNull(msgCtx.getDataInputStream(4));
        assertNull(msgCtx.getDataOutputStream(4));
    }
