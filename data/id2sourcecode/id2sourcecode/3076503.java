    public void postInternal(byte[] portfolioBytes) throws IOException, TransformerException, NoSuchAlgorithmException {
        ByteArrayInputStream portfolioStream = new ByteArrayInputStream(portfolioBytes);
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setErrorListener(new ErrorListener() {

            public void error(TransformerException arg0) throws TransformerException {
                arg0.printStackTrace();
            }

            public void fatalError(TransformerException arg0) throws TransformerException {
                arg0.printStackTrace();
            }

            public void warning(TransformerException arg0) throws TransformerException {
                arg0.printStackTrace();
            }
        });
        URL xsltUrl = getClass().getResource("portfolioPost.xslt");
        InputStream xsltStream = xsltUrl.openStream();
        Source xsltSource = new StreamSource(xsltStream, xsltUrl.toExternalForm());
        Transformer transformer = null;
        transformer = factory.newTransformer(xsltSource);
        Source inputSource = new StreamSource(portfolioStream);
        ByteArrayOutputStream transformed = new ByteArrayOutputStream();
        Result outputResult = new StreamResult(transformed);
        transformer.transform(inputSource, outputResult);
        byte[] outBytes = transformed.toByteArray();
        MessageDigest md;
        String localMD5Sum = null;
        md = MessageDigest.getInstance("MD5");
        md.update(outBytes, 0, outBytes.length);
        localMD5Sum = new BigInteger(1, md.digest()).toString(16);
        logger.info("Local md5: " + localMD5Sum);
        while (localMD5Sum.length() < 32) {
            localMD5Sum = "0" + localMD5Sum;
        }
        URL postRealUrl = new URL(getPostUrl());
        HttpURLConnection postConnection = (HttpURLConnection) postRealUrl.openConnection();
        postConnection.setRequestMethod("POST");
        postConnection.setDoOutput(true);
        postConnection.setRequestProperty("Content-Type", "application/xml");
        if (isB64Gzip()) {
            postConnection.setRequestProperty("Content-Encoding", "b64gzip");
        }
        if (localMD5Sum != null) {
            postConnection.setRequestProperty("Content-md5", localMD5Sum);
        }
        OutputStream postOut = postConnection.getOutputStream();
        isConnected = true;
        logger.info((new Date()).toString() + ": Bundle post connection established");
        if (isB64Gzip()) {
            postOut = new Base64.OutputStream(postOut, Base64.ENCODE);
            postOut = new GZIPOutputStream(postOut);
        }
        logger.info((new Date()).toString() + ": Starting bundle post data transaction");
        postOut.write(outBytes);
        postOut.flush();
        postOut.close();
        InputStream postIn = null;
        try {
            postIn = postConnection.getInputStream();
            logger.info((new Date()).toString() + ": Output stream finished, input stream established.");
        } catch (IOException e) {
            BundlePostException postException = new BundlePostException(postConnection.getResponseCode(), postUrl + ": " + postConnection.getResponseMessage());
            String errorMessage = getErrorMessage(postConnection);
            if (errorMessage != null) {
                postException.setResponseBody(errorMessage);
            }
            throw postException;
        }
        byte[] inBytes = new byte[1000];
        postIn.read(inBytes);
        postIn.close();
        int responseCode = postConnection.getResponseCode();
        String responseMsg = postConnection.getResponseMessage();
        String remoteMD5Sum = postConnection.getHeaderField("Content-md5");
        postConnection.disconnect();
        logger.info((new Date()).toString() + ": Bundle post connection closed");
        if ((responseCode / 100) != 2) {
            throw new BundlePostException(responseCode, postUrl + ": " + responseMsg);
        }
        if (!localMD5Sum.equals(remoteMD5Sum)) {
            String msg = "Bundle MD5 mismatch!\n" + "Local: " + localMD5Sum + "\nRemote: " + remoteMD5Sum;
            logger.severe(msg);
            throw new BundlePostException(responseCode, postUrl + ": " + msg);
        }
    }
