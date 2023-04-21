    public static List<String> currencySymbols() {
        if (allCurrenciesCache != null && System.currentTimeMillis() - lastCacheRead < CACHE_DURATION) {
            return allCurrenciesCache;
        }
        List<String> result = new LinkedList<String>();
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://www.jhall.demon.co.uk/currency/by_currency.html");
            HttpResponse response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inStream = entity.getContent();
                InputStreamReader irs = new InputStreamReader(inStream);
                BufferedReader br = new BufferedReader(irs);
                String l;
                boolean foundTable = false;
                while ((l = br.readLine()) != null) {
                    if (foundTable) {
                        if (l.matches("\\s+<td valign=top>[A-Z]{3}</td>")) {
                            result.add(l.replaceAll(".*top>", "").replaceAll("</td>", ""));
                        }
                    }
                    if (l.startsWith("<h3>Currency Data")) foundTable = true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        allCurrenciesCache = result;
        lastCacheRead = System.currentTimeMillis();
        return result;
    }
