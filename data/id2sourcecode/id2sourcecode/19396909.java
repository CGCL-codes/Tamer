    public int chargeAmount(Long TransactionID, String MSISDN, String refTid, double Amount) {
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
        String pRequestType = "RequestType=chargeAmount";
        String pTransactionID = "TransactionId=" + strTransactionID;
        String pRole = "ReqCred.Role=4";
        String pUserID = "ReqCred.UserId=MASTER";
        String pPIN = "ReqCred.PIN=null";
        String pAccessFrontendID = "AccessFrontendId=VAS";
        String pConsumerID = "ConsumerId=" + MSISDN;
        String pAccountID = "ConsumerAccountId=0";
        String pConsumerPIN = "ConsumerPIN=null";
        String pMerchantID = "MerchantId=MASTER";
        String pProductID = "ProductId=MASTER002";
        String pPurpose = "Purpose=Moj_omiljeni_broj";
        String pCurrency = "Money.Currency=DIN";
        long lAmount = (long) (Math.round(Amount * 1000));
        String strAmount = Long.toString(lAmount);
        String pAmount = "Money.Amount=" + strAmount;
        String spec = servletURL + pRequestType + "&" + pTransactionID + "&" + pRole + "&" + pUserID + "&" + pPIN + "&" + pAccessFrontendID + "&" + pConsumerID + "&" + pAccountID + "&" + pConsumerPIN + "&" + pMerchantID + "&" + pProductID + "&" + pPurpose + "&" + pCurrency + "&" + pAmount;
        System.out.println("Poslato: " + spec);
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
            System.out.println(exc1.toString() + " Method: ChargeAmount; MalformedURLException");
        } catch (IOException exc2) {
            exc2.printStackTrace();
            System.out.println(exc2.toString() + " Method: ChargeAmount; IOException");
        } finally {
            try {
                if (br != null) br.close();
                if (isr != null) isr.close();
                if (urlConnInStr != null) urlConnInStr.close();
            } catch (IOException exc1) {
                exc1.printStackTrace();
                System.out.println(exc1.toString() + " Method: ChargeAmount; IOException");
            }
            return result;
        }
    }
