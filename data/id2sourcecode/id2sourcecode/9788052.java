    private static boolean postRegistrationData(URL url, RegistrationData registration) {
        try {
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
            String timeout = System.getProperty(SVCTAG_CONNECTION_TIMEOUT, "10");
            con.setConnectTimeout(Util.getIntValue(timeout) * 1000);
            if (Util.isVerbose()) {
                System.out.println("Connecting to post registration data at " + url);
            }
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/xml;charset=\"utf-8\"");
            con.connect();
            OutputStream out = null;
            try {
                out = con.getOutputStream();
                registration.storeToXML(out);
                out.flush();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            int returnCode = con.getResponseCode();
            if (Util.isVerbose()) {
                System.out.println("POST return status = " + returnCode);
                printReturnData(con, returnCode);
            }
            return (returnCode == HttpURLConnection.HTTP_OK);
        } catch (MalformedURLException me) {
            InternalError x = new InternalError("Error in registering: " + me.getMessage());
            x.initCause(me);
            throw x;
        } catch (Exception ioe) {
            if (Util.isVerbose()) {
                ioe.printStackTrace();
            }
            return false;
        }
    }
