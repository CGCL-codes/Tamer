    public static void sendSMS(String recipient, String message) {
        try {
            String username = "admin";
            String password = "adminGeoAlert5";
            String originator = "";
            String requestUrl = "http://127.0.0.1:9501/api?action=sendmessage&" + "username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&recipient=" + URLEncoder.encode(recipient, "UTF-8") + "&messagetype=SMS:TEXT" + "&messagedata=" + URLEncoder.encode(message, "UTF-8") + "&originator=" + URLEncoder.encode(originator, "UTF-8") + "&serviceprovider=GSMModem0" + "&responseformat=html";
            URL url = new URL(requestUrl);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            System.out.println(uc.getResponseMessage());
            uc.disconnect();
        } catch (Exception ex) {
        }
    }
