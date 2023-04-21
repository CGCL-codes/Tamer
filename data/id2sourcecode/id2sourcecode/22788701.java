    protected String getAnalysisOutputFromServer(String input) {
        java.io.BufferedWriter bWriter = null;
        URLConnection connection = null;
        String inputXML = null;
        String encryptedInputXML = null;
        Object zippedInput = null;
        String resultString = "";
        bWriter = null;
        connection = null;
        String target = ServletConstant.ANALYSIS_SERVLET;
        Key someKey = CryptoUtil.generateKey();
        String encrptedResult = CryptoUtil.encrypt(getXMLInputString(), someKey);
        ClientObject clientObject = new ClientObject(someKey, getXMLInputString(), DEFAULT_USER_NAME, DEFAULT_PASSWORD);
        String decryptedResult = null;
        try {
            URL url = new URL(target);
            connection = (HttpURLConnection) url.openConnection();
            ((HttpURLConnection) connection).setRequestMethod("POST");
            connection.setDoOutput(true);
            ObjectOutputStream outputToHost = new ObjectOutputStream(connection.getOutputStream());
            outputToHost.writeObject(clientObject);
            outputToHost.flush();
            outputToHost.close();
            java.io.BufferedReader bReader = null;
            bReader = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
            InputStream inputStreamFromServlet = connection.getInputStream();
            ObjectInputStream dataFromServlet = new ObjectInputStream(connection.getInputStream());
            ServerObject serverObject = (ServerObject) dataFromServlet.readObject();
            resultString = serverObject.getStringAttachment();
            ((HttpURLConnection) connection).disconnect();
        } catch (java.io.IOException ex) {
            resultString += ex.toString();
        } catch (Exception e) {
            resultString += e.toString();
        } finally {
            if (bWriter != null) {
                try {
                    bWriter.close();
                } catch (Exception ex) {
                    resultString += ex.toString();
                }
            }
            if (connection != null) {
                try {
                    ((HttpURLConnection) connection).disconnect();
                } catch (Exception ex) {
                    resultString += ex.toString();
                }
            }
            setXMLOutputString(resultString);
        }
        return resultString;
    }
