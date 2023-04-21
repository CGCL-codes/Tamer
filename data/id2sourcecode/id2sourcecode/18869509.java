    public static HttpURLConnection createHTTPConnection(String urlStr, StringBuilder messages) throws MalformedURLException, IOException {
        if (messages == null) {
            messages = new StringBuilder();
        }
        URL url = new URL(urlStr);
        HttpURLConnection uc = null;
        String newProxy = NeissModel.getProperty(PCONST.WEB_PROXY, false);
        String[] split;
        if (newProxy != null && newProxy.length() > 0) {
            split = newProxy.split(",");
            try {
                String proxyURL = split[0];
                int proxyPort = Integer.parseInt(split[1]);
                Proxy prxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyURL, proxyPort));
                messages.append("Setting http proxy (host,port) to: " + proxyURL + "," + proxyPort + "\n");
                uc = (HttpURLConnection) url.openConnection(prxy);
            } catch (NumberFormatException ex) {
                messages.append("PRMWithData.lookForExternalCSV Could not parse the proxy " + "port '" + split[1] + "'.\n");
                return null;
            } catch (ArrayIndexOutOfBoundsException ex) {
                messages.append("PRMWithData.lookForExternalCSV error: the proxy specified (" + newProxy + ") in neissmodel.properties must have at least two elements when split on a " + "comma (the url and port), not " + split.length + "\n");
                return null;
            }
        } else {
            uc = (HttpURLConnection) url.openConnection();
        }
        return uc;
    }
