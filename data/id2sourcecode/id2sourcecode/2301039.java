    public void testGet() {
        try {
            HttpRequest request = new HttpGetRequest("http://localhost/test/printer.php?do=1");
            try {
                HttpResponse response = request.execute();
                System.out.println("response:" + response.getResponseCode() + " " + response.getResponseMessage());
                System.out.println("==========");
                System.out.println("" + response.getResponseDataText());
            } catch (IOException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
