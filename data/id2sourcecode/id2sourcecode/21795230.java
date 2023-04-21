    @Test
    public void testHandleMessageCorruptIntegritySignature() throws Exception {
        KeyPair keyPair = MiscTestUtils.generateKeyPair();
        DateTime notBefore = new DateTime();
        DateTime notAfter = notBefore.plusYears(1);
        X509Certificate certificate = MiscTestUtils.generateCertificate(keyPair.getPublic(), "CN=TestNationalRegistration", notBefore, notAfter, null, keyPair.getPrivate(), true, 0, null, null);
        ServletConfig mockServletConfig = EasyMock.createMock(ServletConfig.class);
        Map<String, String> httpHeaders = new HashMap<String, String>();
        HttpSession mockHttpSession = EasyMock.createMock(HttpSession.class);
        HttpServletRequest mockServletRequest = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(mockServletConfig.getInitParameter("IdentityIntegrityService")).andStubReturn(null);
        EasyMock.expect(mockServletConfig.getInitParameter("IdentityIntegrityServiceClass")).andStubReturn(IdentityIntegrityTestService.class.getName());
        EasyMock.expect(mockServletConfig.getInitParameter("AuditService")).andStubReturn(null);
        EasyMock.expect(mockServletConfig.getInitParameter("AuditServiceClass")).andStubReturn(AuditTestService.class.getName());
        EasyMock.expect(mockServletConfig.getInitParameter("SkipNationalNumberCheck")).andStubReturn(null);
        EasyMock.expect(mockHttpSession.getAttribute(RequestContext.INCLUDE_ADDRESS_SESSION_ATTRIBUTE)).andStubReturn(false);
        EasyMock.expect(mockHttpSession.getAttribute(RequestContext.INCLUDE_CERTIFICATES_SESSION_ATTRIBUTE)).andStubReturn(false);
        EasyMock.expect(mockHttpSession.getAttribute(RequestContext.INCLUDE_PHOTO_SESSION_ATTRIBUTE)).andStubReturn(false);
        EasyMock.expect(mockServletConfig.getInitParameter(IdentityDataMessageHandler.INCLUDE_DATA_FILES)).andReturn(null);
        EasyMock.expect(mockServletRequest.getRemoteAddr()).andStubReturn("remote-address");
        byte[] idFile = "foobar-id-file".getBytes();
        IdentityDataMessage message = new IdentityDataMessage();
        message.idFile = idFile;
        message.identitySignatureFile = "foobar-signature".getBytes();
        message.rrnCertFile = certificate.getEncoded();
        EasyMock.replay(mockServletConfig, mockHttpSession, mockServletRequest);
        AppletServiceServlet.injectInitParams(mockServletConfig, this.testedInstance);
        this.testedInstance.init(mockServletConfig);
        try {
            this.testedInstance.handleMessage(message, httpHeaders, mockServletRequest, mockHttpSession);
            fail();
        } catch (ServletException e) {
            LOG.debug("expected exception: " + e.getMessage());
            EasyMock.verify(mockServletConfig, mockHttpSession, mockServletRequest);
            assertNull(IdentityIntegrityTestService.getCertificate());
            assertEquals("remote-address", AuditTestService.getAuditIntegrityRemoteAddress());
        }
    }
