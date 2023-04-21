    protected String askFreedb(String command) throws FreedbException {
        setupConnection();
        URL url = null;
        try {
            url = new URL("http://" + this.settings.getServer() + ":80/~cddb/cddb.cgi");
        } catch (MalformedURLException e) {
            throw new FreedbException("The URL: " + url + " is invalid, correct the server setting");
        }
        assert url != null;
        URLConnection connection = null;
        try {
            connection = url.openConnection();
            setupProxy(connection);
            connection.setDoOutput(true);
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.println("cmd=" + command + "&hello=" + this.settings.getUserLogin() + "+" + this.settings.getUserDomain() + "+" + this.settings.getClientName() + "+" + this.settings.getClientVersion() + "&proto=" + this.settings.getProtocol());
            out.close();
        } catch (IOException e) {
            throw new FreedbException("Error while trying to connect to freedb server, " + e.getMessage() + ". Check your internet connection settings.");
        }
        assert connection != null;
        String output = null;
        try {
            InputStreamReader isr;
            try {
                isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                isr = new InputStreamReader(connection.getInputStream());
            }
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            output = "";
            while ((inputLine = in.readLine()) != null) output += inputLine + "\n";
            in.close();
        } catch (IOException e) {
            throw new FreedbException("Error while trying read data from freedb server, " + e.getMessage() + ". Check your internet connection settings.");
        }
        assert output != null;
        if (output.startsWith("4") || output.startsWith("5")) throw new FreedbException("Freedb server returned an error: \"" + output + "\"");
        return output;
    }
