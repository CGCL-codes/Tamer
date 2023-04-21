    private RgbDataGrid GetGridFromFile(String filename) throws Exception {
        URL url = new URL(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream())));
        float maxValue = 0;
        String s = reader.readLine();
        ArrayList<Double> xList = new ArrayList<Double>();
        ArrayList<Double> yList = new ArrayList<Double>();
        ArrayList<Float> data = new ArrayList<Float>();
        while (!(s = reader.readLine()).startsWith("Totals")) {
            StringTokenizer st = new StringTokenizer(s, " ");
            float hr = Float.parseFloat(st.nextToken());
            float lat = Float.parseFloat(st.nextToken());
            float value = Float.parseFloat(st.nextToken());
            if (value > maxValue) maxValue = value;
            float lon = hr / 24 * 360;
            if (xList.indexOf(new Double(lon)) < 0) xList.add(new Double(lon));
            if (yList.indexOf(new Double(lat)) < 0) yList.add(new Double(lat));
            data.add(new Float(value));
        }
        reader.close();
        xList.add(new Double(360));
        Double[] lons = xList.toArray(new Double[0]);
        Double[] lats = yList.toArray(new Double[0]);
        for (int i = 0; i < lats.length; i++) {
            data.add(data.get(i));
        }
        RgbDataGrid grid = new RgbDataGrid(lons, lats);
        for (int i = 0; i < data.size(); i++) {
            Color color = new Color(GetColorFromValue(new Double(data.get(i))));
            grid.Data.add(new Integer(color.getRGB()));
        }
        grid.Transpose = true;
        grid.ReverseY = false;
        return grid;
    }
