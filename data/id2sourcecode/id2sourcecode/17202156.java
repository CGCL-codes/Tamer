    static double[] readZoneAttribute(int numZ, String filename, TreeMap<Integer, Integer> zonemap) {
        double[] array = new double[numZ];
        try {
            URL url = filename.getClass().getResource(filename);
            LineNumberReader lnr = new LineNumberReader(new InputStreamReader(url.openStream()));
            String line = null;
            while ((line = lnr.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                Integer zoneid = zonemap.get(Integer.parseInt(st.nextToken()));
                Double value = new Double(st.nextToken());
                array[zoneid] = value;
            }
        } catch (Exception xc) {
            xc.printStackTrace();
        }
        return array;
    }
