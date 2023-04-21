    protected EndpointDescriptor resolveIdpInitiatedSloEndpoint(IdentityProvider idp) throws SSOException {
        Channel defaultChannel = idp.getChannel();
        IdentityMediationEndpoint soapEndpoint = null;
        for (IdentityMediationEndpoint endpoint : defaultChannel.getEndpoints()) {
            if (endpoint.getType().equals(SSOService.IDPInitiatedSingleLogoutService.toString())) {
                if (endpoint.getBinding().equals(SSOBinding.SSO_SOAP.getValue())) {
                    soapEndpoint = endpoint;
                } else if (endpoint.getBinding().equals(SSOBinding.SSO_LOCAL.getValue())) {
                    String location = endpoint.getLocation().startsWith("/") ? defaultChannel.getLocation() + endpoint.getLocation() : endpoint.getLocation();
                    return new EndpointDescriptorImpl(endpoint.getName(), SSOService.IDPInitiatedSingleLogoutService.toString(), SSOBinding.SSO_LOCAL.toString(), location, null);
                }
            }
        }
        if (soapEndpoint != null) {
            String location = soapEndpoint.getLocation().startsWith("/") ? defaultChannel.getLocation() + soapEndpoint.getLocation() : soapEndpoint.getLocation();
            return new EndpointDescriptorImpl(soapEndpoint.getName(), SSOService.IDPInitiatedSingleLogoutService.toString(), SSOBinding.SSO_SOAP.toString(), location, null);
        }
        throw new SSOException("No IDP Initiated SLO endpoint using SOAP binding found!");
    }
