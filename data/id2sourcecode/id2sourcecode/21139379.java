    @Test
    public void GetYahooSearchResult() {
        String query = "Scanner%20Java%20example";
        String request = "http://boss.yahooapis.com/ysearch/web/v1/" + query + "?appid=zfau5aPV34ETbq9mWU0ui5e04y0rIewg1zwvzHb1tGoBFK2nSCU1SKS2D4zphh2rd3Wf&format=xml&count=50";
        try {
            URL url = new URL(request);
            System.out.println("Host : " + url.getHost());
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            String finalContents = "";
            while ((inputLine = reader.readLine()) != null) {
                finalContents += "\n" + inputLine;
            }
            Document doc = Jsoup.parse(finalContents);
            Elements eles = doc.getElementsByTag("url");
            for (Element ele : eles) {
                System.out.println(ele.text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
