    private static OutputStream openOutput(URL thecontext, String thename) throws IOException {
        if (thecontext.getFile().indexOf('/') < 0) throw new java.net.MalformedURLException(thecontext.toString());
        URL theurl = new URL(thecontext, thename);
        String the_absolute_path = theurl.toString();
        log_stream.println("writing remote file " + the_absolute_path);
        return theurl.openConnection().getOutputStream();
    }
