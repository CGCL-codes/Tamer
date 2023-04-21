    public float[] read(String filename, Layer layer, int tIndex, int zIndex, PointList pointList) throws Exception {
        logger.debug("Reading data from " + filename);
        float[] picData = new float[pointList.size()];
        Arrays.fill(picData, Float.NaN);
        FileInputStream fin = null;
        ByteBuffer data = null;
        try {
            fin = new FileInputStream(filename);
            data = ByteBuffer.allocate(ROWS * COLS * 2);
            data.order(ByteOrder.LITTLE_ENDIAN);
            fin.getChannel().read(data);
        } finally {
            try {
                if (fin != null) fin.close();
            } catch (IOException ioe) {
            }
        }
        int picIndex = 0;
        for (ProjectionPoint point : pointList.asList()) {
            LatLonPoint latLon = pointList.getCrsHelper().crsToLatLon(point);
            if (latLon.getLatitude() >= 0.0 && latLon.getLatitude() <= 90.0) {
                int dataIndex = latLonToIndex(latLon.getLatitude(), latLon.getLongitude());
                short val = data.getShort(dataIndex * 2);
                if (val > 0) picData[picIndex] = val;
            }
            picIndex++;
        }
        return picData;
    }
