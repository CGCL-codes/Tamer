    private String searchFreedb(String search) throws FreedbException {
        setupConnection();
        String terms = search.replaceAll(" ", "+");
        URL url = null;
        try {
            url = new URL("http://www.freedb.org/freedb_search.php?words=" + terms + "&allfields=NO&fields=artist&fields=title&allcats=YES&grouping=none");
        } catch (MalformedURLException e) {
            throw new FreedbException("The URL: " + url + " is invalid, remove any accents from the search terms and try again");
        }
        assert url != null;
        URLConnection connection = null;
        try {
            connection = url.openConnection();
            setupProxy(connection);
        } catch (IOException e) {
            throw new FreedbException("Error while trying to connect to freedb server, " + e.getMessage() + ". Check your internet connection settings.");
        }
        assert connection != null;
        String output = null;
        try {
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            output = "";
            while ((inputLine = in.readLine()) != null) output += inputLine + "\n";
            in.close();
        } catch (IOException e) {
            throw new FreedbException("Error while trying read data from freedb server, " + e.getMessage() + ". Check your internet connection settings.");
        }
        assert output != null;
        return output;
    }
