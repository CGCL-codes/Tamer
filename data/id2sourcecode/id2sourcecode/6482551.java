    protected URLConnection openConnection(URL url) throws SavantException {
        Log.log("Opening connection to [" + url + "]", Log.DEBUG);
        URLConnection uc = null;
        try {
            uc = url.openConnection();
            uc.connect();
        } catch (MalformedURLException mue) {
            throw new SavantException(mue);
        } catch (IOException ioe) {
            Log.log("Unable to open connection to [" + url + "] because [" + ioe.getMessage() + "]", Log.INFO);
        }
        return uc;
    }
