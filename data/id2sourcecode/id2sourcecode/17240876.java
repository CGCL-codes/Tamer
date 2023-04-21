    public void invoke(final java.util.Map mapGlobalSettings, final String strComponentName, final java.util.Map mapComponentSettings, final java.util.Map mapInvocationSettings, final IConstants constants, final ISTSRequest request, final ISTSResponse response) {
        this.log.trace("TokenGeneratorHandler::invoke: " + strComponentName);
        if (!this.bConfigured) {
            setWstFault(constants, response, "The specified request failed", "Issue handler not configured");
            return;
        }
        final java.net.URI uriDefaultKeyType = (java.net.URI) mapComponentSettings.get("DefaultKeyType");
        this.log.trace("DefaultKeyType: " + uriDefaultKeyType != null ? uriDefaultKeyType.toString() : null);
        final java.lang.Boolean bIncludeBearerSubjectName = (java.lang.Boolean) mapComponentSettings.get("IncludeBearerSubjectName");
        this.log.trace("IncludeBearerSubjectName: " + bIncludeBearerSubjectName != null ? bIncludeBearerSubjectName.toString() : null);
        final java.net.URI uriTokenIssuer = (java.net.URI) mapComponentSettings.get("TokenIssuer");
        this.log.trace("TokenIssuer: " + uriTokenIssuer != null ? uriTokenIssuer.toString() : null);
        if (null == uriTokenIssuer) {
            setWstFault(constants, response, "The specified request failed", "TokenIssuer not set.");
            return;
        }
        final java.net.URI uriSubjectNameIdentifier = (java.net.URI) mapComponentSettings.get("SubjectNameIdentifierAttribute");
        if (null != uriSubjectNameIdentifier) this.log.trace("SubjectNameIdentifier: " + uriSubjectNameIdentifier != null ? uriSubjectNameIdentifier.toString() : null);
        final java.net.URI uriSubjectNameIdentifierFormat = (java.net.URI) mapComponentSettings.get("SubjectNameIdentifierFormat");
        if (null != uriSubjectNameIdentifierFormat) this.log.trace("SubjectNameIdentifierFormat: " + uriSubjectNameIdentifierFormat != null ? uriSubjectNameIdentifierFormat.toString() : null);
        final java.lang.Boolean bEncryptToken = (java.lang.Boolean) mapComponentSettings.get("EncryptToken");
        this.log.trace("EncryptToken: " + bEncryptToken != null ? bEncryptToken.toString() : null);
        final java.util.List listRST = request.getRequestSecurityTokenCollection();
        final IRequestSecurityToken RST = (IRequestSecurityToken) listRST.get(0);
        final org.eclipse.higgins.sts.api.ILifetime ltLifetime = RST.getLifetime();
        final java.net.URI uriTokenType = RST.getTokenType();
        if (uriTokenType == null || (!OpenIDTokenType.OPENID20_TOKEN.toString().equals(uriTokenType.toString()) && !OpenIDTokenType.OPENID11_TOKEN.toString().equals(uriTokenType.toString()))) {
            setWstFault(constants, response, "Invalid token type", "Cannot handle tokens of type: " + uriTokenType);
            return;
        }
        boolean compat = OpenIDTokenType.OPENID11_TOKEN.equals(uriTokenType.toString());
        final org.eclipse.higgins.sts.api.IAppliesTo appliesToRequest = RST.getAppliesTo();
        java.net.URI uriAppliesTo = null;
        this.log.trace("Checking for AppliesTo");
        if (appliesToRequest != null) {
            this.log.trace("Found AppliesTo");
            final org.eclipse.higgins.sts.api.IEndpointReference eprAppliesTo = appliesToRequest.getEndpointReference();
            uriAppliesTo = eprAppliesTo.getAddress();
        }
        if (uriAppliesTo == null) {
            setWstFault(constants, response, "The specified request failed", "AppliesTo / return_url not found; required for OpenID Tokens.");
            return;
        }
        final org.eclipse.higgins.sts.api.IDigitalIdentity digitalIdentity = RST.getDigitalIdentity();
        if (null == digitalIdentity) {
            setWstFault(constants, response, "The specified request failed", "Digital Subject was not found");
            return;
        }
        final OMFactory omFactory = OMAbstractFactory.getOMFactory();
        final OMNamespace omIdentityNamespace = omFactory.createOMNamespace(constants.getIdentityNamespace().toString(), "ic");
        final OMNamespace omWSTrustNamespace = omFactory.createOMNamespace(constants.getWSTrustNamespace().toString(), "wst");
        final OMElement omRequestedDisplayToken = omFactory.createOMElement("RequestedDisplayToken", omIdentityNamespace);
        final OMElement omDisplayToken = omFactory.createOMElement("DisplayToken", omIdentityNamespace, omRequestedDisplayToken);
        OMElement omRequestedSecurityToken = omFactory.createOMElement("RequestedSecurityToken", omWSTrustNamespace);
        final org.apache.axiom.om.OMElement omRequestedAttachedReference = omFactory.createOMElement("RequestedAttachedReference", omWSTrustNamespace);
        final org.apache.axiom.om.OMElement omRequestedUnattachedReference = omFactory.createOMElement("RequestedUnattachedReference", omWSTrustNamespace);
        final org.apache.axiom.om.OMNamespace omWSSNamespace = omFactory.createOMNamespace(constants.getWSSecurityNamespace().toString(), "wsse");
        final org.apache.axiom.om.OMElement omSecurityTokenReference1 = omFactory.createOMElement("SecurityTokenReference", omWSSNamespace, omRequestedAttachedReference);
        final org.apache.axiom.om.OMElement omSecurityTokenReference2 = omFactory.createOMElement("SecurityTokenReference", omWSSNamespace, omRequestedUnattachedReference);
        final org.apache.axiom.om.OMElement omKeyIdentifier1 = omFactory.createOMElement("KeyIdentifier", omWSSNamespace, omSecurityTokenReference1);
        final org.apache.axiom.om.OMElement omKeyIdentifier2 = omFactory.createOMElement("KeyIdentifier", omWSSNamespace, omSecurityTokenReference2);
        String keyIdentifierValueType = "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1";
        omKeyIdentifier1.addAttribute("ValueType", keyIdentifierValueType, null);
        omKeyIdentifier2.addAttribute("ValueType", keyIdentifierValueType, null);
        String claimedID = null;
        Map attrs = new HashMap();
        final java.util.List listClaims = digitalIdentity.getClaims();
        final java.util.Map mapAttributeClaim = (java.util.Map) mapGlobalSettings.get("AttributeClaimMap");
        String claimTypeUri;
        String value;
        String displayTag;
        Iterator claimsIter = listClaims.iterator();
        while (claimsIter.hasNext()) {
            final IClaim claim = (IClaim) claimsIter.next();
            value = claim.getValues().hasNext() ? (String) claim.getValues().next() : null;
            if (value == null) continue;
            claimTypeUri = claim.getType().getName().toString();
            displayTag = (String) ((Map) mapAttributeClaim.get(claimTypeUri)).get("DisplayName");
            if (OpenIDTokenType.OPENID_CLAIM.equals(claimTypeUri)) {
                claimedID = value;
                addDisplayClaim(claimTypeUri, claimedID, displayTag, omDisplayToken, omIdentityNamespace, omFactory);
                if (compat) break;
            } else if (!compat) {
                attrs.put(claimTypeUri, value);
                addDisplayClaim(claimTypeUri, value, displayTag, omDisplayToken, omIdentityNamespace, omFactory);
            }
        }
        if (claimedID == null) {
            setWstFault(constants, response, "Cannot process OpenID-token RST", "No claimed identifier found.");
            return;
        }
        Association assoc;
        try {
            assoc = _privateAssociations.generate(org.openid4java.association.Association.TYPE_HMAC_SHA1, _expireIn.intValue());
        } catch (AssociationException e) {
            setWstFault(constants, response, "Cannot instantiate private association store", e.getMessage());
            return;
        }
        if (!compat && _opEndpoint == null) {
            setWstFault(constants, response, "Cannot process OpenID-token RST", "OP-Endpoint not configured; required for OpenID 2 messages.");
            return;
        }
        String nonce = _nonceGenerator.next();
        AuthSuccess openidResp;
        try {
            openidResp = AuthSuccess.createAuthSuccess(_opEndpoint, claimedID, claimedID, compat, uriAppliesTo.toString(), nonce, null, assoc, false);
            if (!compat) {
                FetchResponse fetchResp = FetchResponse.createFetchResponse();
                fetchResp.addAttributes(attrs);
                openidResp.addExtension(fetchResp);
            }
            openidResp.setSignature(assoc.sign(openidResp.getSignedText()));
        } catch (OpenIDException e) {
            setWstFault(constants, response, "Cannot generate OpenID assertion", e.getMessage());
            return;
        }
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            setWstFault(constants, response, "Cannot create SHA-1 hash for Requested(Un)AttachedReference", e.getMessage());
            return;
        }
        String sha1base64 = null;
        try {
            sha1base64 = Base64.encode(md.digest(openidResp.keyValueFormEncoding().getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            setWstFault(constants, response, "Unsupported encoding for the OpenID message", e.getMessage());
            return;
        }
        omKeyIdentifier1.setText(sha1base64);
        omKeyIdentifier2.setText(sha1base64);
        final OMNamespace omOpenIDNamespace = omFactory.createOMNamespace(org.openid4java.message.Message.OPENID2_NS, "openid");
        OMElement omOpenIDToken = omFactory.createOMElement("OpenIDToken", omOpenIDNamespace, omRequestedSecurityToken);
        omOpenIDToken.setText(openidResp.keyValueFormEncoding());
        final java.util.List listRSTR = response.getRequestSecurityTokenResponseCollection();
        if (0 == listRSTR.size()) {
            listRSTR.add(new org.eclipse.higgins.sts.common.RequestSecurityTokenResponse());
        }
        final org.eclipse.higgins.sts.api.IRequestSecurityTokenResponse RSTR = (org.eclipse.higgins.sts.common.RequestSecurityTokenResponse) listRSTR.get(0);
        try {
            RSTR.setTokenType(uriTokenType);
            RSTR.setLifetime(ltLifetime);
            RSTR.setRequestedSecurityToken(XMLHelper.toElement(omRequestedSecurityToken));
            RSTR.setRequestedDisplayToken(XMLHelper.toElement(omRequestedDisplayToken));
            RSTR.setRequestedAttachedReference(org.eclipse.higgins.sts.utilities.XMLHelper.toElement(omRequestedAttachedReference));
            RSTR.setRequestedUnattachedReference(org.eclipse.higgins.sts.utilities.XMLHelper.toElement(omRequestedUnattachedReference));
        } catch (final Exception e) {
            org.eclipse.higgins.sts.utilities.ExceptionHelper.Log(this.log, e);
            setWstFault(constants, response, "The specified request failed", "Failed to set RequestSecurityToken elements.");
        }
    }
