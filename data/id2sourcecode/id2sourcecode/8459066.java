    private static LinkedHashMap<String, DatumRange> readOrbits(String sc) throws IOException {
        URL url;
        try {
            if (sc.contains(":")) {
                url = new URL(sc);
            } else {
                url = new URL("http://das2.org/wiki/index.php/Orbits/" + sc);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        InputStream in;
        try {
            in = url.openConnection().getInputStream();
        } catch (IOException ex) {
            if (sc.contains(":")) {
                throw new IllegalArgumentException("I/O Exception prevents reading orbits from \"" + sc + "\"", ex);
            }
            url = Orbits.class.getResource("/orbits/" + sc + ".dat");
            if (url == null) {
                throw new IllegalArgumentException("unable to find orbits file for \"" + sc + "\"");
            }
            try {
                in = url.openConnection().getInputStream();
            } catch (IOException ex2) {
                throw new IllegalArgumentException("I/O Exception prevents reading orbits for \"" + sc + "\"", ex2);
            }
        }
        LinkedHashMap<String, DatumRange> result = new LinkedHashMap();
        BufferedReader rin = new BufferedReader(new InputStreamReader(in));
        String s = rin.readLine();
        int col = -1;
        while (s != null) {
            String[] ss = s.trim().split("\\s+");
            if (ss.length == 3 && (ss[1].startsWith("1") || ss[1].startsWith("2"))) {
                Datum d1;
                Datum d2;
                String s0;
                try {
                    if (col > -1) {
                        try {
                            d1 = TimeUtil.create(ss[col]);
                            d2 = TimeUtil.create(ss[col + 1]);
                            s0 = ss[col == 0 ? 2 : 0];
                        } catch (ParseException ex) {
                            s = rin.readLine();
                            continue;
                        }
                    } else {
                        if (ss[0].length() > 4) {
                            d1 = TimeUtil.create(ss[0]);
                            d2 = TimeUtil.create(ss[1]);
                        } else {
                            throw new ParseException("time is too short", 0);
                        }
                        s0 = ss[2];
                        col = 0;
                    }
                } catch (ParseException ex) {
                    try {
                        d1 = TimeUtil.create(ss[1]);
                        d2 = TimeUtil.create(ss[2]);
                        s0 = ss[0];
                        col = 1;
                    } catch (ParseException ex1) {
                        s = rin.readLine();
                        continue;
                    }
                }
                if (d1.gt(d2)) {
                    System.err.println("dropping invalid orbit: " + s);
                } else {
                    try {
                        result.put(s0, new DatumRange(d1, d2));
                    } catch (IllegalArgumentException ex) {
                        System.err.println(ex.getMessage() + ": " + s);
                    }
                }
            }
            s = rin.readLine();
        }
        rin.close();
        return result;
    }
