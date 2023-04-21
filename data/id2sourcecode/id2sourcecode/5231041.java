    public DigestInfo preSign(List<DigestInfo> digestInfos, List<X509Certificate> signingCertificateChain) throws NoSuchAlgorithmException {
        LOG.debug("preSign");
        HttpServletRequest httpServletRequest;
        try {
            httpServletRequest = (HttpServletRequest) PolicyContext.getContext("javax.servlet.http.HttpServletRequest");
        } catch (PolicyContextException e) {
            throw new RuntimeException("JACC error: " + e.getMessage());
        }
        HttpSession session = httpServletRequest.getSession();
        String signDigestAlgo = (String) session.getAttribute("signDigestAlgo");
        LOG.debug("signature digest algo: " + signDigestAlgo);
        List<String> fileDescriptions = new LinkedList<String>();
        MessageDigest messageDigest = MessageDigest.getInstance(signDigestAlgo, new BouncyCastleProvider());
        for (DigestInfo digestInfo : digestInfos) {
            LOG.debug("processing digest for: " + digestInfo.description);
            fileDescriptions.add(digestInfo.description + "\n");
            messageDigest.update(digestInfo.digestValue);
        }
        byte[] digestValue = messageDigest.digest();
        session.setAttribute("signedFiles", fileDescriptions);
        String description = "Local Test Files";
        return new DigestInfo(digestValue, signDigestAlgo, description);
    }
