    public static String search(String[] keywordsArray) {
        String resultstring = "";
        try {
            URL url = new URL(formSearchURL(keywordsArray));
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStreamReader in = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String s = br.readLine();
            while (s != null) {
                resultstring += s;
                s = br.readLine();
            }
            return resultstring;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
