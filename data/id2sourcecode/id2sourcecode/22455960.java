    public void connectToJiraDB() {
        System.out.println("Running SOAP client...");
        String url = "http://issues.apache.org/jira/rpc/soap/jirasoapservice-v2";
        try {
            SOAPSession soapSession = new SOAPSession(new URL(url));
            soapSession.connect(loginName, loginPassword);
            jiraSoapService = soapSession.getJiraSoapService();
            authToken = soapSession.getAuthenticationToken();
            token = jiraSoapService.login(loginName, loginPassword);
        } catch (java.rmi.RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
