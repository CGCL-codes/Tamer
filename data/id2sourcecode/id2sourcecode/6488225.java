    public byte[] timeStamp(byte[] data, RevocationData revocationData) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(this.digestAlgo);
        byte[] digest = messageDigest.digest(data);
        BigInteger nonce = new BigInteger(128, new SecureRandom());
        TimeStampRequestGenerator requestGenerator = new TimeStampRequestGenerator();
        requestGenerator.setCertReq(true);
        if (null != this.requestPolicy) {
            requestGenerator.setReqPolicy(this.requestPolicy);
        }
        TimeStampRequest request = requestGenerator.generate(this.digestAlgoOid, digest, nonce);
        byte[] encodedRequest = request.getEncoded();
        HttpClient httpClient = new HttpClient();
        if (null != this.username) {
            Credentials credentials = new UsernamePasswordCredentials(this.username, this.password);
            httpClient.getState().setCredentials(AuthScope.ANY, credentials);
        }
        if (null != this.proxyHost) {
            httpClient.getHostConfiguration().setProxy(this.proxyHost, this.proxyPort);
        }
        PostMethod postMethod = new PostMethod(this.tspServiceUrl);
        RequestEntity requestEntity = new ByteArrayRequestEntity(encodedRequest, "application/timestamp-query");
        postMethod.addRequestHeader("User-Agent", this.userAgent);
        postMethod.setRequestEntity(requestEntity);
        int statusCode = httpClient.executeMethod(postMethod);
        if (HttpStatus.SC_OK != statusCode) {
            LOG.error("Error contacting TSP server " + this.tspServiceUrl);
            throw new Exception("Error contacting TSP server " + this.tspServiceUrl);
        }
        Header responseContentTypeHeader = postMethod.getResponseHeader("Content-Type");
        if (null == responseContentTypeHeader) {
            throw new RuntimeException("missing Content-Type header");
        }
        String contentType = responseContentTypeHeader.getValue();
        if (!contentType.startsWith("application/timestamp-reply")) {
            LOG.debug("response content: " + postMethod.getResponseBodyAsString());
            throw new RuntimeException("invalid Content-Type: " + contentType);
        }
        if (0 == postMethod.getResponseContentLength()) {
            throw new RuntimeException("Content-Length is zero");
        }
        InputStream inputStream = postMethod.getResponseBodyAsStream();
        TimeStampResponse timeStampResponse = new TimeStampResponse(inputStream);
        timeStampResponse.validate(request);
        if (0 != timeStampResponse.getStatus()) {
            LOG.debug("status: " + timeStampResponse.getStatus());
            LOG.debug("status string: " + timeStampResponse.getStatusString());
            PKIFailureInfo failInfo = timeStampResponse.getFailInfo();
            if (null != failInfo) {
                LOG.debug("fail info int value: " + failInfo.intValue());
                if (PKIFailureInfo.unacceptedPolicy == failInfo.intValue()) {
                    LOG.debug("unaccepted policy");
                }
            }
            throw new RuntimeException("timestamp response status != 0: " + timeStampResponse.getStatus());
        }
        TimeStampToken timeStampToken = timeStampResponse.getTimeStampToken();
        SignerId signerId = timeStampToken.getSID();
        BigInteger signerCertSerialNumber = signerId.getSerialNumber();
        X500Principal signerCertIssuer = signerId.getIssuer();
        LOG.debug("signer cert serial number: " + signerCertSerialNumber);
        LOG.debug("signer cert issuer: " + signerCertIssuer);
        CertStore certStore = timeStampToken.getCertificatesAndCRLs("Collection", BouncyCastleProvider.PROVIDER_NAME);
        Collection<? extends Certificate> certificates = certStore.getCertificates(null);
        X509Certificate signerCert = null;
        Map<String, X509Certificate> certificateMap = new HashMap<String, X509Certificate>();
        for (Certificate certificate : certificates) {
            X509Certificate x509Certificate = (X509Certificate) certificate;
            if (signerCertIssuer.equals(x509Certificate.getIssuerX500Principal()) && signerCertSerialNumber.equals(x509Certificate.getSerialNumber())) {
                signerCert = x509Certificate;
            }
            String ski = Hex.encodeHexString(getSubjectKeyId(x509Certificate));
            certificateMap.put(ski, x509Certificate);
            LOG.debug("embedded certificate: " + x509Certificate.getSubjectX500Principal() + "; SKI=" + ski);
        }
        if (null == signerCert) {
            throw new RuntimeException("TSP response token has no signer certificate");
        }
        List<X509Certificate> tspCertificateChain = new LinkedList<X509Certificate>();
        X509Certificate certificate = signerCert;
        do {
            LOG.debug("adding to certificate chain: " + certificate.getSubjectX500Principal());
            tspCertificateChain.add(certificate);
            if (certificate.getSubjectX500Principal().equals(certificate.getIssuerX500Principal())) {
                break;
            }
            String aki = Hex.encodeHexString(getAuthorityKeyId(certificate));
            certificate = certificateMap.get(aki);
        } while (null != certificate);
        timeStampToken.validate(tspCertificateChain.get(0), BouncyCastleProvider.PROVIDER_NAME);
        this.validator.validate(tspCertificateChain, revocationData);
        LOG.debug("time-stamp token time: " + timeStampToken.getTimeStampInfo().getGenTime());
        byte[] timestamp = timeStampToken.getEncoded();
        return timestamp;
    }
