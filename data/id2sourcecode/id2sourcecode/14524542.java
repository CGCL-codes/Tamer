    public static String issue(ManagedCard card, Bag requestElements, Locale clientLocale, X509Certificate cert, RSAPrivateKey key, String issuer, SupportedClaims supportedClaimsImpl, String restrictedTo, String relyingPartyCertB64, String messageDigestAlgorithm) throws IOException, CryptoException, TokenIssuanceException {
        Element envelope = new Element(WSConstants.SOAP_PREFIX + ":Envelope", WSConstants.SOAP12_NAMESPACE);
        envelope.addNamespaceDeclaration(WSConstants.WSA_PREFIX, WSConstants.WSA_NAMESPACE_05_08);
        envelope.addNamespaceDeclaration(WSConstants.WSU_PREFIX, WSConstants.WSU_NAMESPACE);
        envelope.addNamespaceDeclaration(WSConstants.WSSE_PREFIX, WSConstants.WSSE_NAMESPACE_OASIS_10);
        envelope.addNamespaceDeclaration(WSConstants.TRUST_PREFIX, WSConstants.TRUST_NAMESPACE_05_02);
        envelope.addNamespaceDeclaration("ic", "http://schemas.xmlsoap.org/ws/2005/05/identity");
        Element header = new Element(WSConstants.SOAP_PREFIX + ":Header", WSConstants.SOAP12_NAMESPACE);
        Element body = new Element(WSConstants.SOAP_PREFIX + ":Body", WSConstants.SOAP12_NAMESPACE);
        envelope.appendChild(header);
        envelope.appendChild(body);
        Element rstr = new Element(WSConstants.TRUST_PREFIX + ":RequestSecurityTokenResponse", WSConstants.TRUST_NAMESPACE_05_02);
        Attribute context = new Attribute("Context", "ProcessRequestSecurityToken");
        rstr.addAttribute(context);
        String tokentype = (String) requestElements.get("tokenType");
        if (tokentype == null) {
            tokentype = WSConstants.SAML11_NAMESPACE;
        }
        Element tokenTypeElt = new Element(WSConstants.TRUST_PREFIX + ":TokenType", WSConstants.TRUST_NAMESPACE_05_02);
        tokenTypeElt.appendChild(tokentype);
        rstr.appendChild(tokenTypeElt);
        log.log(java.util.logging.Level.INFO, "tokentype " + tokentype);
        Element requestType = new Element(WSConstants.TRUST_PREFIX + ":RequestType", WSConstants.TRUST_NAMESPACE_05_02);
        requestType.appendChild("http://schemas.xmlsoap.org/ws/2005/02/trust/Issue");
        rstr.appendChild(requestType);
        Element rst = new Element(WSConstants.TRUST_PREFIX + ":RequestedSecurityToken", WSConstants.TRUST_NAMESPACE_05_02);
        AsymmetricKeyInfo keyInfo = new AsymmetricKeyInfo(cert);
        String signatureAlgorithm = messageDigestAlgorithm + "with" + key.getAlgorithm();
        log.log(java.util.logging.Level.INFO, "signatureAlgorithm=" + signatureAlgorithm);
        ManagedToken token = new ManagedToken(keyInfo, key, tokentype, signatureAlgorithm);
        if (restrictedTo != null) {
            token.setRestrictedTo(restrictedTo, relyingPartyCertB64);
        }
        Set<String> cardClaims = card.getClaims();
        for (String claim : cardClaims) {
            int qm = claim.indexOf('?');
            if (qm > 0) {
                System.out.println("found dynamic claim " + claim + "\n");
                List requestedClaims = requestElements.getValues("claim");
                Iterator iter = requestedClaims.iterator();
                while (iter.hasNext()) {
                    String requestedXClaim = (String) iter.next();
                    String requestedClaim = requestedXClaim.replace("%3F", "?");
                    if (requestedClaim.startsWith(claim)) {
                        System.out.println("requestedClaim " + requestedClaim + " starts with " + claim);
                        token.setClaim(claim, requestedClaim.substring(qm));
                    } else {
                        System.out.println("requestedClaim " + requestedClaim + " does not starts with " + claim);
                    }
                }
            } else {
                List requestedClaims = requestElements.getValues("claim");
                Iterator iter = requestedClaims.iterator();
                while (iter.hasNext()) {
                    String requestedClaim = (String) iter.next();
                    if (claim.equals(requestedClaim)) {
                        String value = card.getClaim(claim);
                        if ((value != null) && !("".equals(value))) {
                            token.setClaim(claim, value);
                            System.out.println("found static claim " + claim + "\n");
                        }
                    }
                }
            }
        }
        String keyType = null;
        {
            List keyTypes = requestElements.getValues("keyType");
            if ((keyTypes != null) && (keyTypes.size() > 0)) {
                keyType = (String) keyTypes.get(0);
            }
        }
        if (keyType != null) {
            log.log(java.util.logging.Level.INFO, "keyType " + keyType);
            if ("http://schemas.xmlsoap.org/ws/2005/05/identity/NoProofKey".equals(keyType)) {
                token.setConfirmationMethod(Subject.BEARER);
            } else if ("http://schemas.xmlsoap.org/ws/2005/02/trust/SymmetricKey".equals(keyType)) {
                log.log(java.util.logging.Level.SEVERE, "keyType " + keyType + "is not supported");
                throw new TokenIssuanceException("keyType " + keyType + "is not supported");
            } else if ("http://schemas.xmlsoap.org/ws/2005/02/trust/PublicKey".equals(keyType)) {
                log.log(java.util.logging.Level.SEVERE, "keyType " + keyType + "is not supported");
                throw new TokenIssuanceException("keyType " + keyType + "is not supported");
            } else {
            }
        }
        String ppid = null;
        {
            List ppids = requestElements.getValues("PPID");
            if ((ppids != null) && (ppids.size() > 0)) {
                ppid = (String) ppids.get(0);
            }
        }
        if (ppid != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String cardPPID = card.getPrivatePersonalIdentifier();
            baos.write(cardPPID.getBytes());
            baos.write(ppid.getBytes());
            String digest = CryptoUtils.digest(baos.toByteArray(), "SHA");
            token.setPrivatePersonalIdentifier(digest);
        } else {
            String cardPPID = card.getPrivatePersonalIdentifier();
            if (relyingPartyCertB64 != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(cardPPID.getBytes());
                baos.write(relyingPartyCertB64.getBytes());
                String digest = CryptoUtils.digest(baos.toByteArray(), "SHA");
                token.setPrivatePersonalIdentifier(digest);
            } else if (restrictedTo != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(restrictedTo.getBytes());
                baos.write(cardPPID.getBytes());
                String digest = CryptoUtils.digest(baos.toByteArray(), "SHA");
                token.setPrivatePersonalIdentifier(digest);
            } else {
                token.setPrivatePersonalIdentifier(cardPPID);
            }
        }
        token.setValidityPeriod(-3, 10);
        token.setIssuer(issuer);
        RandomGUID uuid = new RandomGUID();
        try {
            rst.appendChild(token.serialize(uuid));
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        rstr.appendChild(rst);
        Element requestedAttachedReference = new Element(WSConstants.TRUST_PREFIX + ":RequestedAttachedReference", WSConstants.TRUST_NAMESPACE_05_02);
        Element securityTokenReference = new Element(WSConstants.WSSE_PREFIX + ":SecurityTokenReference", WSConstants.WSSE_NAMESPACE_OASIS_10);
        Element keyIdentifier = new Element(WSConstants.WSSE_PREFIX + ":KeyIdentifier", WSConstants.WSSE_NAMESPACE_OASIS_10);
        Attribute valueType = new Attribute("ValueType", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID");
        keyIdentifier.addAttribute(valueType);
        keyIdentifier.appendChild("uuid-" + uuid.toString());
        securityTokenReference.appendChild(keyIdentifier);
        requestedAttachedReference.appendChild(securityTokenReference);
        rstr.appendChild(requestedAttachedReference);
        Element requestedUnAttachedReference = new Element(WSConstants.TRUST_PREFIX + ":RequestedUnattachedReference", WSConstants.TRUST_NAMESPACE_05_02);
        Element securityTokenReference1 = new Element(WSConstants.WSSE_PREFIX + ":SecurityTokenReference", WSConstants.WSSE_NAMESPACE_OASIS_10);
        Element keyIdentifier1 = new Element(WSConstants.WSSE_PREFIX + ":KeyIdentifier", WSConstants.WSSE_NAMESPACE_OASIS_10);
        Attribute valueType1 = new Attribute("ValueType", "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.0#SAMLAssertionID");
        keyIdentifier1.addAttribute(valueType1);
        keyIdentifier1.appendChild("uuid-" + uuid.toString());
        securityTokenReference1.appendChild(keyIdentifier1);
        requestedUnAttachedReference.appendChild(securityTokenReference1);
        rstr.appendChild(requestedUnAttachedReference);
        Element requestedDisplayToken = new Element(WSConstants.INFOCARD_PREFIX + ":RequestedDisplayToken", WSConstants.INFOCARD_NAMESPACE);
        Element displayToken = new Element(WSConstants.INFOCARD_PREFIX + ":DisplayToken", WSConstants.INFOCARD_NAMESPACE);
        {
            String languageIso639;
            if (clientLocale != null) {
                String uri = cardClaims.iterator().next();
                DbSupportedClaim dbClaim = supportedClaimsImpl.getClaimByUri(uri);
                Locale locale = dbClaim.getLocale(clientLocale);
                languageIso639 = locale.getLanguage();
            } else {
                languageIso639 = "en";
            }
            Attribute lang = new Attribute("xml:lang", "http://www.w3.org/XML/1998/namespace", languageIso639);
            displayToken.addAttribute(lang);
        }
        requestedDisplayToken.appendChild(displayToken);
        for (String uri : cardClaims) {
            String value = card.getClaim(uri);
            DbSupportedClaim dbClaim = supportedClaimsImpl.getClaimByUri(uri);
            String displayTag = dbClaim.getDisplayTag(clientLocale);
            addDisplayClaim(displayToken, uri, displayTag, value);
        }
        addDisplayClaim(displayToken, org.xmldap.infocard.Constants.IC_NAMESPACE, "PPID", card.getPrivatePersonalIdentifier());
        rstr.appendChild(requestedDisplayToken);
        body.appendChild(rstr);
        return envelope.toXML();
    }
