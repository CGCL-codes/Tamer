    public ProcessResponse processData(ProcessRequest signRequest, RequestContext requestContext) throws IllegalRequestException, CryptoTokenOfflineException, SignServerException {
        if (log.isTraceEnabled()) {
            log.trace(">processData");
        }
        ProcessResponse ret = null;
        final ISignRequest sReq = (ISignRequest) signRequest;
        if (!(signRequest instanceof SODSignRequest)) {
            throw new IllegalRequestException("Recieved request wasn't an expected SODSignRequest.");
        }
        final SODSignRequest sodRequest = (SODSignRequest) signRequest;
        final ICryptoToken token = getCryptoToken();
        synchronized (syncObj) {
            int status = token.getCryptoTokenStatus();
            if (log.isDebugEnabled()) {
                log.debug("Crypto token status: " + status);
            }
            if (status != SignerStatus.STATUS_ACTIVE) {
                log.info("Crypto token status is not active, will see if we can autoactivate.");
                String pin = config.getProperty("PIN");
                if (pin == null) {
                    pin = config.getProperty("pin");
                }
                if (pin != null) {
                    log.info("Deactivating and re-activating crypto token.");
                    token.deactivate();
                    try {
                        token.activate(pin);
                    } catch (CryptoTokenAuthenticationFailureException e) {
                        throw new CryptoTokenOfflineException(e);
                    }
                } else {
                    log.info("Autoactivation not enabled, can not re-activate crypto token.");
                }
            }
        }
        final X509Certificate cert = (X509Certificate) getSigningCertificate();
        final PrivateKey privKey = token.getPrivateKey(ICryptoToken.PURPOSE_SIGN);
        final String provider = token.getProvider(ICryptoToken.PURPOSE_SIGN);
        if (cert == null) {
            throw new CryptoTokenOfflineException("No signing certificate");
        }
        if (log.isDebugEnabled()) {
            log.debug("Using signer certificate with subjectDN '" + CertTools.getSubjectDN(cert) + "', issuerDN '" + CertTools.getIssuerDN(cert) + ", serNo " + CertTools.getSerialNumberAsString(cert));
        }
        final SODFile sod;
        try {
            final String digestAlgorithm = config.getProperty(PROPERTY_DIGESTALGORITHM, DEFAULT_DIGESTALGORITHM);
            final String digestEncryptionAlgorithm = config.getProperty(PROPERTY_SIGNATUREALGORITHM, DEFAULT_SIGNATUREALGORITHM);
            if (log.isDebugEnabled()) {
                log.debug("Using algorithms " + digestAlgorithm + ", " + digestEncryptionAlgorithm);
            }
            final String doHashing = config.getProperty(PROPERTY_DODATAGROUPHASHING, DEFAULT_DODATAGROUPHASHING);
            final Map<Integer, byte[]> dgvalues = sodRequest.getDataGroupHashes();
            Map<Integer, byte[]> dghashes = dgvalues;
            if (StringUtils.equalsIgnoreCase(doHashing, "true")) {
                if (log.isDebugEnabled()) {
                    log.debug("Converting data group values to hashes using algorithm " + digestAlgorithm);
                }
                dghashes = new HashMap<Integer, byte[]>(16);
                for (Integer dgId : dgvalues.keySet()) {
                    byte[] value = dgvalues.get(dgId);
                    if (log.isDebugEnabled()) {
                        log.debug("Hashing data group " + dgId + ", value is of length: " + value.length);
                    }
                    if ((value != null) && (value.length > 0)) {
                        MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
                        byte[] result = digest.digest(value);
                        if (log.isDebugEnabled()) {
                            log.debug("Resulting hash is of length: " + result.length);
                        }
                        dghashes.put(dgId, result);
                    }
                }
            }
            String ldsVersion = config.getProperty(PROPERTY_LDSVERSION, DEFAULT_LDSVERSION);
            String unicodeVersion = config.getProperty(PROPERTY_UNICODEVERSION);
            final String ldsVersionRequest = sodRequest.getLdsVersion();
            if (ldsVersionRequest != null) {
                ldsVersion = ldsVersionRequest;
            }
            final String unicodeVersionRequest = sodRequest.getUnicodeVersion();
            if (unicodeVersionRequest != null) {
                unicodeVersion = unicodeVersionRequest;
            }
            if ("0107".equals(ldsVersion)) {
                ldsVersion = null;
                unicodeVersion = null;
            } else if ("0108".equals(ldsVersion)) {
                if (unicodeVersion == null) {
                    throw new IllegalRequestException("Unicode version must be specified in LDS version 1.8");
                }
            } else {
                throw new IllegalRequestException("Unsupported LDS version: " + ldsVersion);
            }
            if (log.isDebugEnabled()) {
                log.debug("LDS version: " + ldsVersion + ", unicodeVerison: " + unicodeVersion);
            }
            final SODFile constructedSod = new SODFile(digestAlgorithm, digestEncryptionAlgorithm, dghashes, privKey, cert, provider, ldsVersion, unicodeVersion);
            sod = new SODFile(new ByteArrayInputStream(constructedSod.getEncoded()));
        } catch (NoSuchAlgorithmException ex) {
            throw new SignServerException("Problem constructing SOD", ex);
        } catch (CertificateException ex) {
            throw new SignServerException("Problem constructing SOD", ex);
        } catch (IOException ex) {
            throw new SignServerException("Problem reconstructing SOD", ex);
        }
        try {
            verifySignatureAndChain(sod, getSigningCertificateChain());
            if (log.isDebugEnabled()) {
                log.debug("SOD verified correctly, returning SOD.");
            }
            final byte[] signedbytes = sod.getEncoded();
            String fp = CertTools.getFingerprintAsString(signedbytes);
            ret = new SODSignResponse(sReq.getRequestID(), signedbytes, cert, fp, new ArchiveData(signedbytes));
        } catch (GeneralSecurityException e) {
            log.error("Error verifying the SOD we signed ourselves. ", e);
            throw new SignServerException("SOD verification failure", e);
        }
        if (log.isTraceEnabled()) {
            log.trace("<processData");
        }
        return ret;
    }
