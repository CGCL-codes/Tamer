    protected EndpointDescriptor resolveIdpInitiatedSloEndpoint(IdentityProvider idp) throws SSOException {
        Channel defaultChannel = idp.getChannel();
        IdentityMediationEndpoint e = null;
        for (IdentityMediationEndpoint endpoint : defaultChannel.getEndpoints()) {
            if (endpoint.getType().equals(SSOService.IDPInitiatedSingleLogoutService.toString())) {
                if (endpoint.getBinding().equals(SSOBinding.SSO_LOCAL.getValue())) {
                    String location = endpoint.getLocation().startsWith("/") ? defaultChannel.getLocation() + endpoint.getLocation() : endpoint.getLocation();
                    return new EndpointDescriptorImpl(identityProvider.getName() + "-sso-slo-soap", SSOService.IDPInitiatedSingleLogoutService.toString(), SSOBinding.SSO_LOCAL.toString(), location, null);
                } else if (endpoint.getBinding().equals(SSOBinding.SSO_LOCAL.getValue())) {
                    e = endpoint;
                }
            }
        }
        if (e != null) {
            String location = e.getLocation().startsWith("/") ? defaultChannel.getLocation() + e.getLocation() : e.getLocation();
            return new EndpointDescriptorImpl(identityProvider.getName() + "-sso-slo-soap", SSOService.IDPInitiatedSingleLogoutService.toString(), e.getBinding(), location, null);
        }
        throw new SSOException("No IDP Initiated SLO endpoint using SOAP binding found!");
    }
