    public static BigDecimal convertFromTo(String fromCurrency, String toCurrency) {
        List<String> valid = currencySymbols();
        if (!valid.contains(fromCurrency)) {
            throw new IllegalArgumentException(String.format("Invalid from currency: %s", fromCurrency));
        }
        if (!valid.contains(toCurrency)) {
            throw new IllegalArgumentException(String.format("Invalid to currency: %s", toCurrency));
        }
        String url = String.format("http://www.gocurrency.com/v2/dorate.php?inV=1&from=%s&to=%s&Calculate=Convert", fromCurrency, toCurrency);
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            StringBuilder result = new StringBuilder();
            if (entity != null) {
                InputStream inStream = entity.getContent();
                InputStreamReader irs = new InputStreamReader(inStream);
                BufferedReader br = new BufferedReader(irs);
                String l;
                while ((l = br.readLine()) != null) {
                    result.append(l);
                }
            }
            String theWholeThing = result.toString();
            int start = theWholeThing.lastIndexOf("<div id=\"converter_results\"><ul><li>");
            String substring = result.substring(start);
            int startOfInterestingStuff = substring.indexOf("<b>") + 3;
            int endOfInterestingStuff = substring.indexOf("</b>", startOfInterestingStuff);
            String interestingStuff = substring.substring(startOfInterestingStuff, endOfInterestingStuff);
            String[] parts = interestingStuff.split("=");
            String value = parts[1].trim().split(" ")[0];
            BigDecimal bottom = new BigDecimal(value);
            return bottom;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
