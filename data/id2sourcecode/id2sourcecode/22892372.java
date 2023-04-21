    void loadURL(String url, Vector destAddresses, Vector destPorts, Vector destPasswds, Hashtable confRes) throws IOException, ApMonException {
        System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
        System.setProperty("sun.net.client.defaultReadTimeout", "5000");
        URL destURL = null;
        try {
            destURL = new URL(url);
        } catch (MalformedURLException e) {
            throw new ApMonException(e.getMessage());
        }
        URLConnection urlConn = destURL.openConnection();
        long lmt = urlConn.getLastModified();
        confRes.put(new URL(url), new Long(lmt));
        logger.info("Loading from URL " + url + "...");
        BufferedReader br = new BufferedReader(new InputStreamReader(destURL.openStream()));
        String destLine;
        while ((destLine = br.readLine()) != null) {
            String tmp2 = destLine.trim();
            if (tmp2.length() == 0 || tmp2.startsWith("#")) continue;
            if (tmp2.startsWith("xApMon_loglevel")) {
                StringTokenizer lst = new StringTokenizer(tmp2, " =");
                lst.nextToken();
                setLogLevel(lst.nextToken());
                continue;
            }
            if (tmp2.startsWith("xApMon_")) {
                parseXApMonLine(tmp2);
                continue;
            }
            addToDestinations(tmp2, destAddresses, destPorts, destPasswds);
        }
        br.close();
    }
