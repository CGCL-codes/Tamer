    @Test
    public void SimpleParse() {
        try {
            URL url = new URL("http://www.javapractices.com/topic/TopicAction.do?Id=87");
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            String finalContents = "";
            while ((inputLine = reader.readLine()) != null) {
                finalContents += "\n" + inputLine.replaceAll("<code", "<pre").replaceAll("code>", "pre>");
            }
            Document doc = Jsoup.parse(finalContents);
            Elements eles = doc.getElementsByTag("pre");
            for (Element ele : eles) {
                System.out.println(ele.text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
