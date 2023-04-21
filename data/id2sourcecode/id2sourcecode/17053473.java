    public String getMessage() {
        _magicNumber++;
        try {
            URL url = new URL("http://" + _destHost + ":" + _destPort + "/WAIT" + encodeURL(" " + _magicNumber));
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Proxy-Authorization", "Basic " + _proxyEncodedIdentification);
            connection.setUseCaches(false);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            System.err.println("Waiting for a message...");
            String message = in.readLine();
            if (message.indexOf(ServerProtocol.SESSION) != -1) {
                _session = message.substring(message.indexOf("=") + 1, message.indexOf(" ") - 1);
                return message.substring(message.indexOf(" ") + 1);
            } else return message;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
