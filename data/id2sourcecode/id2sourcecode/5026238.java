    public Date getTimeStamp(X509Certificate cert) throws IOException, TSPException, SignatureTimestampException {
        TimeStampRequestGenerator generator = new TimeStampRequestGenerator();
        generator.setCertReq(true);
        byte[] nonce = new byte[16];
        r.nextBytes(nonce);
        byte[] emptyBuffer = new byte[new SHA1Digest().getDigestSize()];
        TimeStampRequest req = generator.generate(TSPAlgorithms.SHA1, emptyBuffer, new BigInteger(nonce));
        byte encoded[] = req.getEncoded();
        URL url = getURLPrincipal(cert);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Host", url.getHost() + ":" + url.getPort());
        conn.setRequestProperty("Content-type", "application/timestamp-query");
        conn.setRequestProperty("Content-length", Integer.toString(encoded.length));
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
        return response.getTimeStampToken().getTimeStampInfo().getGenTime();
    }
