    private static String getData(String myurl) throws Exception {
        URL url = new URL(myurl);
        uc = (HttpURLConnection) url.openConnection();
        uc.setRequestProperty("Cookie", phpsessioncookie);
        br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String temp = "", k = "";
        while ((temp = br.readLine()) != null) {
            k += temp;
        }
        br.close();
        return k;
    }
