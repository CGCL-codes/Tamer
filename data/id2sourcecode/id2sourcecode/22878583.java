    public void testUpdate() throws Exception {
        PushApp pushApp = new PushApp();
        pushApp.setAppId("mock-app-id");
        String oid = this.pushAppController.create(pushApp);
        log.info("**********************************************");
        log.info("OID: " + oid);
        assertNotNull(oid);
        log.info("**********************************************");
        pushApp.setCertificate("certificate".getBytes());
        pushApp.setCertificatePassword("password");
        pushApp.addChannel("channel1");
        pushApp.addChannel("channel2");
        this.pushAppController.update(pushApp);
        pushApp = this.pushAppController.readPushApp("mock-app-id");
        String certificate = new String(pushApp.getCertificate());
        assertEquals(certificate, "certificate");
        String certificatePassword = pushApp.getCertificatePassword();
        assertEquals(certificatePassword, "password");
        Set<String> channels = pushApp.getChannels();
        assertEquals(channels.size(), 2);
        Set<String> newChannels = new HashSet<String>();
        newChannels.add("newchannel");
        pushApp.setChannels(newChannels);
        this.pushAppController.update(pushApp);
        pushApp = this.pushAppController.readPushApp("mock-app-id");
        channels = pushApp.getChannels();
        assertEquals(pushApp.getChannels().size(), 1);
    }
