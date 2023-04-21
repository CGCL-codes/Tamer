    private void tsaRequest() throws Exception {
        final Random rand = new Random();
        final TimeStampRequestGenerator timeStampRequestGenerator = new TimeStampRequestGenerator();
        boolean doRun = true;
        do {
            final int nonce = rand.nextInt();
            byte[] digest = new byte[20];
            if (instring != null) {
                final byte[] digestBytes = instring.getBytes("UTF-8");
                final MessageDigest dig = MessageDigest.getInstance(TSPAlgorithms.SHA1, "BC");
                dig.update(digestBytes);
                digest = dig.digest();
                doRun = false;
            }
            if (infilestring != null) {
                digest = digestFile(infilestring, TSPAlgorithms.SHA1);
                doRun = false;
            }
            final byte[] hexDigest = Hex.encode(digest);
            if (LOG.isDebugEnabled()) {
                LOG.debug("MessageDigest=" + new String(hexDigest));
            }
            final TimeStampRequest timeStampRequest;
            if (inreqstring == null) {
                LOG.debug("Generating a new request");
                timeStampRequestGenerator.setCertReq(certReq);
                if (reqPolicy != null) {
                    timeStampRequestGenerator.setReqPolicy(reqPolicy);
                }
                timeStampRequest = timeStampRequestGenerator.generate(TSPAlgorithms.SHA1, digest, BigInteger.valueOf(nonce));
            } else {
                LOG.debug("Reading request from file");
                timeStampRequest = new TimeStampRequest(readFiletoBuffer(inreqstring));
            }
            final byte[] requestBytes = timeStampRequest.getEncoded();
            if (outreqstring != null) {
                byte[] outBytes;
                if (base64) {
                    outBytes = Base64.encode(requestBytes);
                } else {
                    outBytes = requestBytes;
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(outreqstring);
                    fos.write(outBytes);
                } finally {
                    if (fos != null) {
                        fos.close();
                    }
                }
            }
            keyStoreOptions.setupHTTPS();
            URL url;
            URLConnection urlConn;
            DataOutputStream printout;
            DataInputStream input;
            url = new URL(urlstring);
            final long startTime = System.nanoTime();
            urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/timestamp-query");
            printout = new DataOutputStream(urlConn.getOutputStream());
            printout.write(requestBytes);
            printout.flush();
            printout.close();
            input = new DataInputStream(urlConn.getInputStream());
            while (input.available() == 0) {
                Thread.sleep(100);
            }
            byte[] ba = null;
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            do {
                if (ba != null) {
                    baos.write(ba);
                }
                ba = new byte[input.available()];
            } while (input.read(ba) != -1);
            final long estimatedTime = System.nanoTime() - startTime;
            LOG.info("Got reply after " + TimeUnit.NANOSECONDS.toMillis(estimatedTime) + " ms");
            final byte[] replyBytes = baos.toByteArray();
            if (outrepstring != null) {
                byte[] outBytes;
                if (base64) {
                    outBytes = Base64.encode(replyBytes);
                } else {
                    outBytes = replyBytes;
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(outrepstring);
                    fos.write(outBytes);
                } finally {
                    if (fos != null) {
                        fos.close();
                    }
                }
            }
            final TimeStampResponse timeStampResponse = new TimeStampResponse(replyBytes);
            timeStampResponse.validate(timeStampRequest);
            LOG.info("TimeStampRequest validated");
            if (LOG.isDebugEnabled()) {
                LOG.debug("(Status: " + timeStampResponse.getStatus() + ", " + timeStampResponse.getFailInfo() + "): " + timeStampResponse.getStatusString());
            }
            if (doRun) {
                Thread.sleep(sleep);
            }
        } while (doRun);
    }
