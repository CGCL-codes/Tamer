    public static InputStream obtainDocumentURL(Selenium selenium, String URL, boolean URLRelativa, ITestContext contextTNG) throws IOException, Exception {
        String cookie = selenium.getCookie();
        if (URLRelativa) {
            String dominio = selenium.getEval("window.document.domain");
            String location = selenium.getLocation();
            int posDominio = location.indexOf(dominio);
            URL = location.substring(0, posDominio) + dominio + URL;
        }
        try {
            HostnameVerifier hv = new HostnameVerifier() {

                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            URL url = new URL(URL);
            if (URL.indexOf("https") == 0) {
                if (((String) contextTNG.getAttribute("proxyPort")).compareTo("") != 0) {
                    System.setProperty("https.proxyHost", "muscat");
                    System.setProperty("https.proxyPort", "8080");
                }
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestProperty("Cookie", cookie);
                return con.getInputStream();
            } else {
                if (((String) contextTNG.getAttribute("proxyPort")).compareTo("") != 0) {
                    System.setProperty("http.proxyHost", "muscat");
                    System.setProperty("http.proxyPort", "8080");
                }
                URLConnection con = url.openConnection();
                con.setRequestProperty("Cookie", cookie);
                return con.getInputStream();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
