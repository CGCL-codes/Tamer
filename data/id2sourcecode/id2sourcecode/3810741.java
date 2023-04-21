    @Override
    public List<Float> read(String filename, Layer layer, int tIndex, int zIndex, Domain<HorizontalPosition> domain) throws IOException {
        logger.debug("Reading data from " + filename);
        List<Float> picData = WmsUtils.nullArrayList(domain.getDomainObjects().size());
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
        for (HorizontalPosition point : domain.getDomainObjects()) {
            LonLatPosition lonLat = Utils.transformToWgs84LonLat(point);
            if (lonLat.getLatitude() >= 0.0 && lonLat.getLatitude() <= 90.0) {
                int dataIndex = latLonToIndex(lonLat.getLatitude(), lonLat.getLongitude());
                short val = data.getShort(dataIndex * 2);
                if (val > 0) picData.set(picIndex, (float) val);
            }
            picIndex++;
        }
        return picData;
    }
