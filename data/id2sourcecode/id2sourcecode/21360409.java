    @Override
    public QDataSet getDataSet(ProgressMonitor mon) throws Exception {
        String colname = params.get("arg_0");
        params.remove("arg_0");
        String surl = this.resourceURL.toString() + "?" + URLSplit.formatParams(params);
        System.err.println(surl);
        URLConnection urlc;
        urlc = new URL(surl).openConnection();
        String userInfo = urlc.getURL().getUserInfo();
        if (userInfo != null) {
            String encode = new String(Base64.encodeBytes(urlc.getURL().getUserInfo().getBytes()));
            urlc.setRequestProperty("Authorization", "Basic " + encode);
        }
        mon.started();
        String[] colNames = null;
        int[] colOffsets = null;
        int[] colLengths = null;
        InputStream in = urlc.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            String s = reader.readLine();
            if (!s.equals("UNIFIED_OUTPUT_FOLLOWS")) {
                throw new IllegalArgumentException("response didn't start with UNIFIED_OUTPUT_FOLLOWS");
            }
            while (!s.startsWith("colnames")) {
                s = reader.readLine();
            }
            String scolnames = s.substring(10);
            colNames = scolnames.split(",");
            colLengths = new int[colNames.length];
            colOffsets = new int[colNames.length];
            for (int i = 0; i < colNames.length; i++) {
                colOffsets[i] = i;
                colLengths[i] = 1;
            }
            s = reader.readLine();
            if (s == null) {
                throw new IllegalArgumentException("no records returned");
            }
            AsciiParser parser = new AsciiParser();
            parser.guessDelimParser(s);
            parser.setValidMin(-1.9e37);
            WritableDataSet wds = parser.readStream(reader, s, mon);
            QDataSet ttag = getTimes(wds);
            int i;
            for (i = colNames.length - 1; i >= 0; i--) {
                if (columnNameEqual(colNames[i], colname)) break;
            }
            if (i == -1) throw new IllegalArgumentException("bad column parameter: expected one of " + scolnames);
            MutablePropertyDataSet data;
            if (colLengths[i] == 1) {
                data = DataSetOps.slice1(wds, colOffsets[i]);
            } else {
                data = DataSetOps.trim(wds, colOffsets[i], colLengths[i]);
            }
            data.putProperty(QDataSet.DEPEND_0, ttag);
            return data;
        } catch (Exception e) {
            throw e;
        } finally {
            reader.close();
            mon.finished();
        }
    }
