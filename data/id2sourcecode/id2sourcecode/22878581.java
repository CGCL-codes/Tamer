    public void testRead() throws Exception {
        PushApp app = new PushApp();
        app.setAppId("mock-app-id");
        app.setCertificate("cert".getBytes());
        app.setCertificatePassword("cert-password");
        for (int i = 0; i < 5; i++) {
            app.addChannel("push_channel_" + i);
        }
        String oid = this.pushAppController.create(app);
        log.info("**********************************************");
        log.info("OID: " + oid);
        assertNotNull(oid);
        app = this.pushAppController.readPushApp("mock-app-id");
        byte[] certificate = app.getCertificate();
        assertNotNull(certificate);
        String certificateStr = new String(certificate);
        assertEquals(certificateStr, "cert");
        log.info("Certificate: " + certificateStr);
        String certPassword = app.getCertificatePassword();
        assertEquals(certPassword, "cert-password");
        log.info("Cert-Password: " + certPassword);
        Set<String> channels = app.getChannels();
        assertNotNull(channels);
        for (String channel : channels) {
            log.info("Channel: " + channel);
        }
        log.info("**********************************************");
    }
