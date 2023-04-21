    public int getPR(String domain) {
        int result = -1;
        GetGooglePrHash gHash = new GetGooglePrHash();
        String googlePrResult = "";
        long hash = gHash.hash(("info:" + domain).getBytes());
        String urlstring = "http://" + GOOGLE_PR_DATACENTER_IPS[dataCenterIdx] + "/search?client=navclient-auto&hl=en&" + "ch=6" + hash + "&ie=UTF-8&oe=UTF-8&features=Rank&q=info:" + domain;
        URL url = null;
        HttpURLConnection uconn = null;
        BufferedReader bin = null;
        InputStream is = null;
        try {
            while (true) {
                url = new URL(urlstring);
                uconn = (HttpURLConnection) url.openConnection();
                is = uconn.getInputStream();
                if (is == null) continue; else break;
            }
            byte[] buff = new byte[1024];
            int read = is.read(buff);
            while (read > 0) {
                googlePrResult = new String(buff, 0, read);
                read = is.read(buff);
            }
            if (googlePrResult == "" && !domain.startsWith("http://")) {
                PageRankService prService = new PageRankService();
                domain = "http://" + domain;
                return prService.getPR(domain);
            }
            googlePrResult = googlePrResult.split(":")[2].trim();
            result = new Long(googlePrResult).intValue();
        } catch (Exception e) {
        }
        dataCenterIdx++;
        if (dataCenterIdx == GOOGLE_PR_DATACENTER_IPS.length) {
            dataCenterIdx = 0;
        }
        return result;
    }
