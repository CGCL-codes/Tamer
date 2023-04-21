    private String URLExplorer(URL exploreurl) {
        BufferedReader proquestreader = null;
        try {
            proquestreader = new BufferedReader(new InputStreamReader(exploreurl.openStream()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Scanner s = new Scanner(proquestreader);
        Boolean done = false;
        Boolean started = false;
        String contents = null;
        s.useDelimiter("[\n\r]+");
        while (s.hasNext() && !done) {
            String next = s.next();
            if (next.contains("<!--Start FULL TEXT-->")) {
                contents = next.substring(next.indexOf("<!--Start FULL TEXT-->"), next.indexOf("<!--End FULL TEXT-->"));
                started = true;
                done = true;
            }
        }
        if (s != null) {
            s.close();
        }
        if (proquestreader != null) {
            try {
                proquestreader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return contents;
    }
