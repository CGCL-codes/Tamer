    private static synchronized String sendRequest(String request, boolean useAuthorization, boolean getResponse) {
        String response = "";
        try {
            URL url = new URL("http://" + hostName + ":" + portNumber + request);
            URLConnection urlC = url.openConnection();
            if (useAuthorization) {
                String loginPassword = "admin:admin";
                BASE64Encoder enc = new sun.misc.BASE64Encoder();
                urlC.setRequestProperty("Authorization", "Basic " + enc.encode(loginPassword.getBytes()));
            }
            InputStream content = (InputStream) urlC.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(content));
            if (getResponse) {
                int car = in.read();
                while ((car != -1) && (car != (int) 'D')) {
                    response += new Character((char) car).toString();
                    car = in.read();
                }
            }
            while ((in.readLine()) != null) {
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return response;
    }
