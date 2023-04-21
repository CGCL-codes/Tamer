    public int rechargeAmount(Long TransactionID, String MSISDN, String Purpose, double Amount, long ExpiryDate, String Merchant_id) {
        int result = -1;
        BufferedReader br = null;
        InputStreamReader isr = null;
        InputStream urlConnInStr = null;
        String strTransactionID = TransactionID.toString();
        int length = strTransactionID.length();
        String baseOfTransactionID = "999";
        for (int i = 0; i < (11 - length); i++) {
            baseOfTransactionID = baseOfTransactionID + "0";
        }
        strTransactionID = baseOfTransactionID + strTransactionID;
        String pRequestType = "RequestType=rechargeAmount";
        String pTransactionID = "TransactionId=" + strTransactionID;
        String pRole = "ReqCred.Role=4";
        String pUserID = "ReqCred.UserId=";
        String pPIN = "ReqCred.PIN=null";
        String pAccessFrontendID = "AccessFrontendId=mts";
        String pConsumerID = "ConsumerId=" + MSISDN;
        String pAccountID = "ConsumerAccountId=0";
        String pConsumerPIN = "ConsumerPIN=null";
        String pPurpose = "Purpose";
        try {
            pPurpose = "Purpose=" + URLEncoder.encode(Purpose, encodingType);
            pUserID = "ReqCred.UserId=" + URLEncoder.encode(Merchant_id, encodingType);
        } catch (UnsupportedEncodingException exc1) {
            exc1.printStackTrace();
            return -1;
        }
        String pCurrency = "Money.Currency=DIN";
        long lAmount = (long) (Amount * 1000);
        String strAmount = Long.toString(lAmount);
        String pAmount = "Money.Amount=" + strAmount;
        String pExpiryDate = "ExpiryDate=" + Long.toString(ExpiryDate);
        String spec = servletURL + pRequestType + "&" + pTransactionID + "&" + pRole + "&" + pUserID + "&" + pPIN + "&" + pAccessFrontendID + "&" + pConsumerID + "&" + pAccountID + "&" + pConsumerPIN + "&" + pPurpose + "&" + pCurrency + "&" + pAmount + "&" + pExpiryDate;
        try {
            URL url = new URL(spec);
            URLConnection urlConn = url.openConnection();
            urlConnInStr = urlConn.getInputStream();
            isr = new InputStreamReader(urlConnInStr);
            br = new BufferedReader(isr);
            String line = br.readLine();
            if (line != null) {
                result = getExecutionStatus(line);
            }
        } catch (MalformedURLException exc1) {
            exc1.printStackTrace();
        } catch (IOException exc2) {
            exc2.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
                if (isr != null) isr.close();
                if (urlConnInStr != null) urlConnInStr.close();
            } catch (IOException exc1) {
                exc1.printStackTrace();
            }
            return result;
        }
    }
