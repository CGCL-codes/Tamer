    @Override
    boolean doActivate() {
        setTimeout(15000);
        String report = Test.URLEncodeAll(step - 1);
        String urlstring = getAddress() + "/" + page + "?" + report;
        try {
            url = new URL(urlstring);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        try {
            yc = url.openConnection();
            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        } catch (IOException e) {
            System.err.println("UploadTest.doActivate:" + e.getMessage());
            return false;
        }
        return true;
    }
