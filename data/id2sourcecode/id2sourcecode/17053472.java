    public boolean sendMessage(String msg) {
        _magicNumber++;
        try {
            URL url = new URL("http://" + _destHost + ":" + _destPort + "/" + encodeURL(msg + " " + _magicNumber + (_session == null ? "" : ("?session=" + _session))));
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Proxy-Authorization", "Basic " + _proxyEncodedIdentification);
            connection.setUseCaches(false);
            connection.getInputStream().close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
