    public PaymentInformation getPaymentInformation(Token token) {
        try {
            LOG.debug("Retrieving information about transaction [transactionId={}]", token.getGatewayTransactionId());
            QuickPayMd5SumPrinter md5 = new QuickPayMd5SumPrinter();
            HttpPost post = new HttpPost(apiUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(md5.getBasicNameValuePair("protocol", protocolVersion));
            nvps.add(md5.getBasicNameValuePair("msgtype", "status"));
            nvps.add(md5.getBasicNameValuePair("merchant", token.getMerchant().getGatewayUserId()));
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
            QuickPayResult qpresult = new QuickPayResult(result);
            List<PaymentInformation.HistoryEntry> history = new ArrayList();
            return new PaymentInformation(getStatusFromState(Integer.parseInt(qpresult.getParameter("state"))), history, qpresult.getParameter("ordernumber"), Integer.parseInt(qpresult.getParameter("amount")), qpresult.getParameter("currency"), qpresult.getParameter("qpstat") + ": " + qpresult.getParameter("qpstatmsg"), qpresult.getParameter("merchant"), qpresult.getParameter("merchantemail"), qpresult.getParameter("transaction"), getCardTypeFromString(qpresult.getParameter("cardtype")));
        } catch (IOException ex) {
            LOG.error("Unable to get status for payment.", ex);
            throw new PaymentException("Unable to get status for payment.", ex);
        }
    }
