    public static String fromSingle(String IPString) {
        URL url;
        try {
            url = new URL(dbAddr + IPString);
            URLConnection urlConn = url.openConnection();
            urlConn.connect();
            Scanner inc = new Scanner(urlConn.getInputStream());
            try {
                return inc.nextLine();
            } catch (NoSuchElementException e) {
            }
        } catch (MalformedURLException e) {
            System.err.println("Error getting Location");
        } catch (IOException e) {
            System.err.println("Error getting Location");
        }
        return "Error (NONE)";
    }
