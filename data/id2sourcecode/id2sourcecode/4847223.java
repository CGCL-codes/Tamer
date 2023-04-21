    public void getPlayerDetails() {
        URL url = null;
        LoginData loginData = new LoginData();
        try {
            String strUrl = loginData.getServer() + COMMON + PLAYERDETAILS;
            System.out.println(strUrl);
            System.out.println(loginData.getCookie());
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            return;
        }
        try {
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", USERAGENT);
            connection.setRequestProperty("Cookie", loginData.getCookie());
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host");
            return;
        } catch (IOException e) {
            System.out.println("Error in opening URLConnection, Reading or Writing");
            return;
        }
    }
