    @Test
    public void testHandleReturnResponse() throws Exception {
        SAML2ProtocolServiceAuthIdent saml2ProtocolService = new SAML2ProtocolServiceAuthIdent();
        String userId = UUID.randomUUID().toString();
        String givenName = "test-given-name";
        String surName = "test-sur-name";
        Identity identity = new Identity();
        identity.name = surName;
        identity.firstName = givenName;
        identity.gender = Gender.MALE;
        identity.dateOfBirth = new GregorianCalendar();
        identity.nationality = "BELG";
        identity.placeOfBirth = "Gent";
        HttpSession mockHttpSession = EasyMock.createMock(HttpSession.class);
        HttpServletRequest mockHttpServletRequest = EasyMock.createMock(HttpServletRequest.class);
        HttpServletResponse mockHttpServletResponse = EasyMock.createMock(HttpServletResponse.class);
        ServletContext mockServletContext = EasyMock.createMock(ServletContext.class);
        KeyPair keyPair = generateKeyPair();
        DateTime notBefore = new DateTime();
        DateTime notAfter = notBefore.plusMonths(1);
        X509Certificate certificate = generateSelfSignedCertificate(keyPair, "CN=Test", notBefore, notAfter);
        IdPIdentity idpIdentity = new IdPIdentity("test", new KeyStore.PrivateKeyEntry(keyPair.getPrivate(), new Certificate[] { certificate }));
        IdentityProviderConfiguration mockConfiguration = EasyMock.createMock(IdentityProviderConfiguration.class);
        mockServletContext.setAttribute(AbstractSAML2ProtocolService.IDP_CONFIG_CONTEXT_ATTRIBUTE, mockConfiguration);
        EasyMock.expect(mockHttpSession.getServletContext()).andReturn(mockServletContext);
        EasyMock.expect(mockServletContext.getAttribute(AbstractSAML2ProtocolService.IDP_CONFIG_CONTEXT_ATTRIBUTE)).andReturn(mockConfiguration);
        EasyMock.expect(mockHttpSession.getAttribute(AbstractSAML2ProtocolService.TARGET_URL_SESSION_ATTRIBUTE)).andStubReturn("http://127.0.0.1");
        EasyMock.expect(mockHttpSession.getAttribute(AbstractSAML2ProtocolService.RELAY_STATE_SESSION_ATTRIBUTE)).andStubReturn("relay-state");
        EasyMock.expect(mockHttpSession.getAttribute(AbstractSAML2ProtocolService.IN_RESPONSE_TO_SESSION_ATTRIBUTE)).andStubReturn("a77a1c87-e590-47d7-a3e0-afea455ebc01");
        EasyMock.expect(mockHttpSession.getAttribute(AbstractSAML2ProtocolService.ISSUER_SESSION_ATTRIBUTE)).andStubReturn("Issuer");
        EasyMock.expect(mockHttpServletRequest.getSession()).andReturn(mockHttpSession);
        EasyMock.expect(mockHttpSession.getServletContext()).andReturn(mockServletContext);
        EasyMock.expect(mockServletContext.getAttribute(AbstractSAML2ProtocolService.IDP_CONFIG_CONTEXT_ATTRIBUTE)).andReturn(mockConfiguration);
        EasyMock.expect(mockConfiguration.getResponseTokenValidity()).andStubReturn(5);
        EasyMock.expect(mockConfiguration.findIdentity()).andStubReturn(idpIdentity);
        EasyMock.expect(mockConfiguration.getIdentityCertificateChain()).andStubReturn(Collections.singletonList(certificate));
        EasyMock.expect(mockConfiguration.getDefaultIssuer()).andStubReturn("TestIssuer");
        EasyMock.replay(mockHttpServletRequest, mockHttpSession, mockServletContext, mockConfiguration);
        saml2ProtocolService.init(mockServletContext, mockConfiguration);
        ReturnResponse returnResponse = saml2ProtocolService.handleReturnResponse(mockHttpSession, userId, new HashMap<String, Attribute>(), null, null, null, mockHttpServletRequest, mockHttpServletResponse);
        EasyMock.verify(mockHttpServletRequest, mockHttpSession, mockServletContext, mockConfiguration);
        assertNotNull(returnResponse);
        assertEquals("http://127.0.0.1", returnResponse.getActionUrl());
        List<NameValuePair> attributes = returnResponse.getAttributes();
        assertNotNull(attributes);
        NameValuePair relayStateAttribute = null;
        NameValuePair samlResponseAttribute = null;
        for (NameValuePair attribute : attributes) {
            if ("RelayState".equals(attribute.getName())) {
                relayStateAttribute = attribute;
                continue;
            }
            if ("SAMLResponse".equals(attribute.getName())) {
                samlResponseAttribute = attribute;
                continue;
            }
        }
        assertNotNull(relayStateAttribute);
        assertEquals("relay-state", relayStateAttribute.getValue());
        assertNotNull("no SAMLResponse attribute", samlResponseAttribute);
        String encodedSamlResponse = samlResponseAttribute.getValue();
        assertNotNull(encodedSamlResponse);
        String samlResponse = new String(Base64.decodeBase64(encodedSamlResponse));
        LOG.debug("SAML response: " + samlResponse);
        File tmpFile = File.createTempFile("saml-response-", ".xml");
        FileUtils.writeStringToFile(tmpFile, samlResponse);
        LOG.debug("tmp file: " + tmpFile.getAbsolutePath());
    }
