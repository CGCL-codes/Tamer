    public static final Reader url2stream(String f, boolean quiet) {
        Reader stream = null;
        try {
            URL url = new URL(f);
            stream = toReader(url.openStream());
        } catch (MalformedURLException e) {
            if (quiet) return null;
            IO.errmes("bad URL: " + f, e);
        } catch (IOException e) {
            if (quiet) return null;
            IO.errmes("unable to read URL: " + f, e);
        }
        return stream;
    }
