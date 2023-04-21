    public void testJOSSO11WebSSOEndpointsAreConfiguredProperly() throws Exception {
        AbstractCamelEndpoint endpoint = resolveMandatoryEndpoint("josso-binding:JOSSO11AuthnRequestToSAMLR2?channelRef=ABC");
        assertEquals("getChannelRef", "ABC", endpoint.getChannelRef());
    }
