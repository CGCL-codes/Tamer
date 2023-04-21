    private int getCurrentLocation_google_map_api(double[] location, int cellID, int lac) throws Exception {
        int result = Constants.DAO_OK;
        String urlString = Constants.GoogleCellID_URL;
        URL url;
        url = new URL(urlString);
        URLConnection conn = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) conn;
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setConnectTimeout(10000);
        httpConn.connect();
        OutputStream outputStream = httpConn.getOutputStream();
        WriteData(outputStream, cellID, lac);
        InputStream inputStream = httpConn.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        dataInputStream.readShort();
        dataInputStream.readByte();
        int code = dataInputStream.readInt();
        if (code == 0) {
            double lat = (double) dataInputStream.readInt() / 1000000D;
            double lng = (double) dataInputStream.readInt() / 1000000D;
            dataInputStream.readInt();
            dataInputStream.readInt();
            dataInputStream.readUTF();
            location[Constants.Latitude] = lat;
            location[Constants.Longitude] = lng;
        } else {
            result = Constants.DAO_ERROR;
        }
        return result;
    }
