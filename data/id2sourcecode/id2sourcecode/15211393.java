    public static String getCity(final String ip, final boolean more) throws IOException {
        if ("127.0.0.1".equals(ip) || "localhost".equals(ip)) {
            return "";
        }
        String city = (String) ips.get(ip);
        if (city != null) {
            if (!more) {
                final int e = city.indexOf(" ");
                if (e > 0) {
                    return city.substring(0, e);
                }
            }
            return city;
        }
        try {
            final URL url = new URL("http://www.cz88.net/ip/?ip=" + ip);
            final String htmlString = IoUtils.getStringFromInputStream(url.openStream(), "gbk");
            final Element first = Jsoup.parse(htmlString).select("#InputIPAddrMessage").first();
            if (first != null) {
                ips.put(ip, city = first.text());
                return city;
            }
        } catch (final IOException e) {
        }
        return "";
    }
