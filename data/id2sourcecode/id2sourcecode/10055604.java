    public void testSimpleCaseNoChannels() throws Exception {
        List<String> channels = new ArrayList<String>();
        Device device = DeviceController.getInstance().read("IMEI:8675309");
        ExecutionContext.getInstance().setDevice(device);
        Request request = new Request("iphone_push_callback");
        request.setAttribute("os", "iphone");
        request.setAttribute("deviceToken", "device-token");
        request.setAttribute("appId", "blah");
        request.setListAttribute("channels", channels);
        this.service.invoke(request);
        device = DeviceController.getInstance().read("IMEI:8675309");
        String deviceToken = device.getDeviceToken();
        assertNotNull(deviceToken);
        assertEquals(deviceToken, "device-token");
        PushApp pushApp = PushAppController.getInstance().readPushApp("blah");
        assertNotNull(pushApp);
        Set<String> storedChannels = pushApp.getChannels();
        assertTrue(storedChannels == null || storedChannels.isEmpty());
        Set<String> devices = pushApp.getDevices();
        assertTrue(devices != null && !devices.isEmpty());
        String deviceId = devices.iterator().next();
        assertEquals(deviceId, device.getIdentifier());
    }
