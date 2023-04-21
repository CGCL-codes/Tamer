    public static void main(String[] args) throws Exception {
        URL url = new URL("http://www.papco.org:8080/opendap/onera_cdf/lanl_1990_95/LANL_1990_095_H0_SOPA_ESP_19980505_V01.cdf.das");
        MyDASParser parser = new MyDASParser();
        parser.parse(url.openStream());
    }
