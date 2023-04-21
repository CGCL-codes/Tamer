    @Test
    public void testWSSecurity() throws Exception {
        KeyPair keyPair = TestUtils.generateKeyPair();
        X509Certificate certificate = TestUtils.generateSelfSignedCertificate(keyPair, "CN=Test");
        KeyPair fooKeyPair = TestUtils.generateKeyPair();
        X509Certificate fooCertificate = TestUtils.generateSelfSignedCertificate(fooKeyPair, "CN=F00");
        this.wsSecurityClientHandler.setServerCertificate(certificate);
        KeyStoreType keyStoreType = KeyStoreType.PKCS12;
        String keyStorePassword = "secret";
        String keyEntryPassword = "secret";
        String alias = "alias";
        File tmpP12File = File.createTempFile("keystore-", ".p12");
        tmpP12File.deleteOnExit();
        TestUtils.persistInKeyStore(tmpP12File, "pkcs12", keyPair.getPrivate(), certificate, keyStorePassword, keyEntryPassword, alias);
        String keyStorePath = tmpP12File.getAbsolutePath();
        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
        InputStream testSoapMessageInputStream = WSSecurityTest.class.getResourceAsStream("/test-soap-message.xml");
        assertNotNull(testSoapMessageInputStream);
        SOAPMessage message = messageFactory.createMessage(null, testSoapMessageInputStream);
        SOAPMessageContext soapMessageContext = new TestSOAPMessageContext(message, true);
        soapMessageContext.put(MessageContext.SERVLET_CONTEXT, this.mockServletContext);
        expect(this.mockServletContext.getAttribute(TrustService.class.getName())).andReturn(mockTrustService);
        expect(this.mockTrustService.getWsSecurityConfig()).andReturn(new WSSecurityConfigEntity("test", true, keyStoreType, keyStorePath, keyStorePassword, keyEntryPassword, alias));
        replay(this.mockObjects);
        assertTrue(this.wsSecurityServerHandler.handleMessage(soapMessageContext));
        verify(this.mockObjects);
        SOAPMessage resultMessage = soapMessageContext.getMessage();
        SOAPPart resultSoapPart = resultMessage.getSOAPPart();
        LOG.debug("signed SOAP part:" + TestUtils.domToString(resultSoapPart));
        Element nsElement = resultSoapPart.createElement("nsElement");
        nsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
        nsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        nsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
        nsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
        Node resultNode = XPathAPI.selectSingleNode(resultSoapPart, "/soap:Envelope/soap:Header/wsse:Security[@soap:mustUnderstand = '1']", nsElement);
        assertNotNull(resultNode);
        assertNotNull("missing WS-Security timestamp", XPathAPI.selectSingleNode(resultSoapPart, "/soap:Envelope/soap:Header/wsse:Security/wsu:Timestamp/wsu:Created", nsElement));
        assertEquals(2.0, XPathAPI.eval(resultSoapPart, "count(//ds:Reference)", nsElement).num());
        soapMessageContext.put(MessageContext.MESSAGE_OUTBOUND_PROPERTY, false);
        assertTrue(this.wsSecurityClientHandler.handleMessage(soapMessageContext));
        this.wsSecurityClientHandler.setServerCertificate(fooCertificate);
        try {
            this.wsSecurityClientHandler.handleMessage(soapMessageContext);
            fail();
        } catch (SOAPFaultException e) {
            LOG.debug("SOAPFaultException: " + e.getMessage());
        }
    }
