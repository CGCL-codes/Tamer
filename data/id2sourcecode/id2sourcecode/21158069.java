    public void loadData() {
        try {
            String tags = "aa2a5";
            tags += "bb2b3";
            tags += "b4b6c";
            tags += "c1c3c6";
            tags += "c8dd1";
            tags += "d2ee1";
            tags += "e7e8e9";
            tags += "gh";
            tags += "jkg1";
            tags += "g3g4g5";
            tags += "g6ii5";
            tags += "j1j3j4";
            tags += "j5j6k1";
            tags += "k2k3k4";
            tags += "k5ll1";
            tags += "l2l3m";
            tags += "m2m3m4";
            tags += "m5m6m7";
            tags += "m8nn4";
            tags += "opp1";
            tags += "p2p5p6qrr1r2r5r6";
            tags += "r7ss1s7t1t6t7t8vv1v7ww1w4xy";
            final String url = "http://finance.yahoo.com/d/quotes.csv?s=" + symbol + "&f=" + tags;
            final URLConnection conn = (new URL(url)).openConnection();
            conn.connect();
            final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String str = in.readLine();
            values = new NullReturningArrayList<String>();
            final String[] split = str.split(",");
            for (String s : split) {
                if (s.length() > 254) {
                    s = s.substring(0, 254);
                }
                values.add(s);
            }
            in.close();
        } catch (final IOException ioe) {
            logger.error("Error parsing quote csv", ioe);
        }
    }
