    protected EnzymeList downloadRebase() throws IOException {
        final String FTP_REBASE = "ftp://ftp.neb.com/pub/rebase/";
        String url = FTP_REBASE + "VERSION";
        message("Fetching " + url);
        BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        String line = in.readLine();
        in.close();
        if (line == null) throw new IOException("Cannot get latest version in " + url);
        message("Version " + line);
        url = FTP_REBASE + "allenz." + line.trim();
        message("Fetching " + url);
        in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        EnzymeList el = EnzymeList.read(in);
        in.close();
        return el;
    }
