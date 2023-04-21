    private TimeStampToken generateTimeStamp(X509Certificate cert, byte digest[], String digestAlgorithm) throws IOException, TSPException, SignatureTimestampException {
        try {
            TimeStampRequestGenerator generator = new TimeStampRequestGenerator();
            generator.setCertReq(true);
            byte[] nonce = new byte[16];
            r.nextBytes(nonce);
            TimeStampRequest req = generator.generate(digestAlgorithm, digest, new BigInteger(nonce));
            byte encoded[] = req.getEncoded();
            URL url = getURLPrincipal(cert);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/timestamp-query");
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.write(encoded);
            out.flush();
            out.close();
            InputStream in = conn.getInputStream();
            TimeStampResponse response = new TimeStampResponse(in);
            in.close();
            response.validate(req);
            TimeStampToken token = response.getTimeStampToken();
            byte[] data = token.toCMSSignedData().getEncoded();
            TimeStampTokenInfo info = token.getTimeStampInfo();
            AttributeTable atable = token.getSignedAttributes();
            Attribute a = atable.get(new DERObjectIdentifier("1.2.840.113549.1.9.16.2.14"));
            return response.getTimeStampToken();
        } catch (Exception e) {
            throw new SignatureTimestampException(e);
        }
    }
