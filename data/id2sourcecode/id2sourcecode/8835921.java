    public static URLAccess doURLAccess(String urlLocation) {
        URLAccess res = new URLAccess();
        res.urlLocation = urlLocation;
        try {
            res.url = new URL(urlLocation);
            PushbackInputStream in = new PushbackInputStream(new BufferedInputStream(res.url.openStream()), 1);
            int b = in.read();
            in.unread(b);
            res.inputStream = in;
        } catch (MalformedURLException e) {
            res.errorMessage = "Malformed URL";
        } catch (FileNotFoundException e) {
            res.errorMessage = "File Not Found";
        } catch (UnknownHostException e) {
            res.errorMessage = "Unknown Host";
        } catch (ConnectException e) {
            res.errorMessage = "Connection Timed Out";
        } catch (IOException e) {
            res.errorMessage = "IO exception";
        }
        return res;
    }
