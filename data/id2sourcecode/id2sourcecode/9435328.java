    private static void loadValues() {
        if (!isLoaded) {
            System.out.println("**************************************1");
            String mobilealerts = SystemFilesLoader.getInstance().getNewgenlibProperties().getProperty("MOBILE_ALERTS", "");
            System.out.println("**************************************2");
            if (!mobilealerts.equals("") && mobilealerts.trim().equals("YES")) {
                System.out.println("**************************************3");
                String vsplid = SystemFilesLoader.getInstance().getNewgenlibProperties().getProperty("VERUS_SUBSCRIPTION_ID", "");
                String vsplvercode = SystemFilesLoader.getInstance().getNewgenlibProperties().getProperty("VERUS_VERIFICATION_CODE", "");
                System.out.println("**************************************4");
                File SMSURL = new File(NewGenLibRoot.getRoot() + "/SystemFiles/SMS_URL");
                if (SMSURL.exists()) {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(SMSURL));
                        while (br.ready()) rawURL += br.readLine();
                        br.close();
                    } catch (Exception e) {
                    }
                }
                if (!vsplid.equals("") && !vsplvercode.equals("") && rawURL.equals("")) {
                    try {
                        System.out.println("**************************************5");
                        System.out.println("");
                        URL url = new URL("http://www.verussolutions.biz/mobility.php?Id=" + vsplid + "&verificationCode=" + vsplvercode);
                        URLConnection urconn = url.openConnection();
                        System.out.println("**************************************6");
                        if (ProxySettings.getInstance().isProxyAvailable()) {
                            System.out.println("Proxy settings set................................sdfsdfsd");
                            urconn.setRequestProperty("Proxy-Authorization", "Basic " + ProxySettings.getInstance().getEncodedPassword());
                        }
                        System.out.println("**************************************7");
                        urconn.setDoOutput(true);
                        System.out.println("**************************************8");
                        OutputStream os = urconn.getOutputStream();
                        os.flush();
                        System.out.println("**************************************9");
                        InputStream is = urconn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String response = "";
                        System.out.println("**************************************10");
                        while (br.ready()) {
                            response += br.readLine();
                        }
                        br.close();
                        JSONObject jb = null;
                        try {
                            jb = new JSONObject(response);
                        } catch (Exception expp) {
                            expp.printStackTrace();
                            staticError = ERR_INVALID_SUBSCRPTION_DETAILS;
                        }
                        if (jb != null) {
                            String proceed = jb.getString("PROCEED");
                            if (proceed.equals("YES")) {
                                rawURL = jb.getString("RAWURL");
                            } else {
                                staticError = ERR_SMS_SUBSCRIPTION_EXPIRED;
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        staticError = ERR_NO_CONNECTIVITY;
                    }
                }
            }
            isLoaded = true;
        }
    }
