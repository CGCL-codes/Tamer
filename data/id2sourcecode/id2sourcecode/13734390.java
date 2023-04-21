    public ArrayList getGridData(String request) throws AddeURLException {
        URL url;
        gridHeaders = new ArrayList();
        gridData = new ArrayList();
        try {
            url = new URL(request);
            urlc = url.openConnection();
            InputStream is = urlc.getInputStream();
            dis = new DataInputStream(new BufferedInputStream(is));
        } catch (AddeURLException ae) {
            throw new AddeURLException("Dataset not found: " + ae);
        } catch (Exception e) {
            throw new AddeURLException("Error opening connection: " + e);
        }
        int numBytes = ((AddeURLConnection) urlc).getInitialRecordSize();
        if (numBytes == 0) {
            status = -1;
            throw new AddeURLException("No datasets found");
        }
        try {
            int check;
            byte[] header = new byte[256];
            while (numBytes == 4) {
                check = dis.readInt();
                if (check != HEARTBEAT) {
                    System.out.println("problem...not heartbeat = " + check);
                }
                numBytes = dis.readInt();
            }
            int checkBytes = dis.readInt();
            if (checkBytes != numBytes) {
                throw new AddeURLException("Invalid number of bytes returned for grid.");
            }
            int numGrids = dis.readInt();
            for (int i = 0; i < numGrids; i++) {
                dis.readFully(header, 0, 256);
                String name = new String(header, 24, 4);
                McIDASGridDirectory mg = new McIDASGridDirectory(header);
                System.out.println(mg.toString());
                CoordinateSystem c = mg.getCoordinateSystem();
                gridHeaders.add(mg);
                int rows = mg.getRows();
                int cols = mg.getColumns();
                double scale = mg.getParamScale();
                double[] ddata = new double[rows * cols];
                int n = 0;
                for (int nc = 0; nc < cols; nc++) {
                    for (int nr = 0; nr < rows; nr++) {
                        int temp = dis.readInt();
                        ddata[(rows - nr - 1) * cols + nc] = (temp == McIDASUtil.MCMISSING) ? Double.NaN : ((double) temp) / scale;
                    }
                }
                gridData.add(ddata);
                check = dis.readInt();
                if (check != 0) break;
            }
        } catch (Exception re) {
            System.out.println(re);
        }
        return gridData;
    }
