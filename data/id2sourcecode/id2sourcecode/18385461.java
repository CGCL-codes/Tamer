    public void okIlkDenemeBu() {
        try {
            URL con = new URL("http://studivz.net");
            URLConnection urlCon = con.openConnection();
            urlCon.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            String decodedString;
            while ((decodedString = in.readLine()) != null) {
                String qs = "<title>";
                System.out.println("xx:" + decodedString);
            }
            in.close();
        } catch (IOException e) {
        }
    }
