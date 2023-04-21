    private static Iterator<String> parse(Class service, URL url) throws IOException {
        InputStream in = null;
        BufferedReader r = null;
        ArrayList<String> names = new ArrayList<String>();
        try {
            in = url.openStream();
            r = new BufferedReader(new InputStreamReader(in, "utf-8"));
            int lc = 1;
            while ((lc = parseLine(service, url, r, lc, names)) >= 0) ;
        } finally {
            if (r != null) r.close();
            if (in != null) in.close();
        }
        return names.iterator();
    }
