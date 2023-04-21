    public String getConsumerAccountList(Long TransactionID, String msisdns) {
        String result = null;
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
        String pRequestType = "RequestType=getConsumerAccountList";
        String pTransactionID = "TransactionId=" + strTransactionID;
        String pRole = "ReqCred.Role=6";
        String pUserID = "ReqCred.UserId=INQUIRY-C";
        String pPIN = "ReqCred.PIN=";
        String pAccessFrontendID = "AccessFrontendId=HTTP";
        String pUserIdList = "UserIdList=" + msisdns;
        String pAccountID = "AccountIdList=0";
        String pPINList = "PINList=";
        String spec = servletURL + pRequestType + "&" + pTransactionID + "&" + pRole;
        spec += "&" + pUserID + "&" + pPIN + "&" + pAccessFrontendID + "&" + pUserIdList + "&";
        spec += pAccountID + "&" + pPINList;
        System.out.println("Poslato: " + spec);
        try {
            URL url = new URL(spec);
            URLConnection urlConn = url.openConnection();
            urlConnInStr = urlConn.getInputStream();
            isr = new InputStreamReader(urlConnInStr);
            br = new BufferedReader(isr);
            String line = "";
            while (true) {
                String l = br.readLine();
                if (l == null) break;
                line += l + "\r\n";
            }
            if (line != null) {
                result = getBalance(line);
            }
        } catch (MalformedURLException exc1) {
            exc1.printStackTrace();
            System.out.println(exc1.toString() + " Method: GetTAState; MalformedURLException");
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
