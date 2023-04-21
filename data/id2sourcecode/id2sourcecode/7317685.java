    @Override
    public void capture(Token token, long amountInCents) {
        try {
            LOG.debug("Capturing money for transaction [transactionId={}; amountInCents={}]", new Object[] { token.getGatewayTransactionId(), amountInCents });
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(apiUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "capture"));
            nvps.add(md5.getBasicNameValuePair("merchant", token.getMerchant().getGatewayUserId()));
            nvps.add(md5.getBasicNameValuePair("amount", "" + amountInCents));
            nvps.add(md5.getBasicNameValuePair("finalize", "1"));
            nvps.add(md5.getBasicNameValuePair("transaction", token.getGatewayTransactionId()));
            if (testMode) {
                nvps.add(md5.getBasicNameValuePair("testmode", "1"));
            }
            md5.add(token.getMerchant().getGatewaySecret());
            nvps.add(new BasicNameValuePair("md5check", md5.getMD5Result()));
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse response = getHttpClient().execute(post);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream ba = new ByteArrayOutputStream((int) entity.getContentLength());
            entity.writeTo(ba);
            String result = new String(ba.toByteArray(), 0, ba.size());
            checkQuickpayResult(new QuickPayResult(result));
        } catch (IOException ex) {
            LOG.error("Unable to capture payment.", ex);
            throw new PaymentException("Unable to capture payment.", ex);
        }
    }
