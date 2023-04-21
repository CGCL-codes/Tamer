    public ExchangeService(URL upURL, String login, String password) {
        super();
        srvAddr = Address.newServerAddress("upload");
        try {
            URL url = new URL(upURL, "messengerLogin?login=" + login + "&password=" + password + "&do=Login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamPump.doPump(conn.getInputStream(), baos);
            String resp = new String(baos.toByteArray());
            conn.disconnect();
            System.out.println("Got response: " + resp);
            if (resp.startsWith("+")) sessionKey = resp.substring(1); else sessionKey = null;
            if (cMsgr == null) {
                cMsgr = new ClientMessenger(new URL(upURL, "messenger?SESSID=" + sessionKey));
                cMsgr.addConnectionStateListener(this);
            }
            sender = new FileUploadClient(new UploadClientProxy(cMsgr, "fileUpload"));
            cMsgr.setConnected(true);
            Thread.sleep(1000);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
