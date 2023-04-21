    synchronized void grab(Double lat, Double lng, String pano_id, int xid) throws Exception {
        System.out.println("grabbing...");
        InputStream in = null;
        int response = -1;
        boolean fTurning = xid < 0 ? false : true;
        try {
            URL url;
            if (pano_id == null) {
                url = new URL("http://maps.google.co.jp/cbk?output=xml&cb_client=maps_sv&ll=" + lat + "%2C" + lng);
            } else {
                url = new URL("http://cbk0.google.com/cbk?output=xml&panoid=" + pano_id);
            }
            URLConnection conn = url.openConnection();
            if (!(conn instanceof HttpURLConnection)) throw new IOException("Not an HTTP connection.");
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setConnectTimeout(CONNECT_TIMEOUT);
            httpConn.setReadTimeout(SOCKET_TIMEOUT);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
                try {
                    SAXParserFactory spfactory = SAXParserFactory.newInstance();
                    SAXParser parser = spfactory.newSAXParser();
                    CbSaxHandler cbsh = new CbSaxHandler();
                    parser.parse(in, cbsh);
                    System.out.println("------------>" + cbsh.getPanoId());
                    pano_id = cbsh.getPanoId();
                    for (int i = 0; i < MAX_LINK; i++) {
                        pano_id_link[i] = cbsh.getPanoId(i);
                    }
                    mLat = cbsh.getLat();
                    mLng = cbsh.getLng();
                    if (xid < 0) {
                        double YaYa;
                        double Ya = cbsh.getYawDeg();
                        mXid = xid = mXidPrev;
                        for (int i = 0; i < 7; i++) {
                            YaYa = Ya + DIVIDED_DEG * (i - 3) - DIVIDED_DEG / 2;
                            if (YaYa < 0.0) {
                                YaYa += 360.0;
                            } else if (YaYa > 360.0) {
                                YaYa -= 360.0;
                            }
                            if (YaYa <= mDirPrev && mDirPrev <= (YaYa + DIVIDED_DEG)) {
                                mXid = xid = i;
                                break;
                            }
                        }
                    }
                    mDir = cbsh.getYawDeg() + DIVIDED_DEG * (mXid - 3);
                    if (mDir < 0.0) {
                        mDir += 360.0;
                    } else if (mDir > 360.0) {
                        mDir -= 360.0;
                    }
                    mDirPrev = mDir;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
            }
        }
        if (pano_id != null) {
            try {
                in = null;
                response = -1;
                URL url = new URL("http://cbk0.google.com/cbk?output=tile&panoid=" + pano_id + "&zoom=3&x=" + xid + "&y=1");
                URLConnection conn = url.openConnection();
                if (!(conn instanceof HttpURLConnection)) throw new IOException("Not an HTTP connection.");
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setConnectTimeout(CONNECT_TIMEOUT);
                httpConn.setReadTimeout(SOCKET_TIMEOUT);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                    File file = new File("cap.jpg");
                    FileOutputStream fos = new FileOutputStream(file);
                    System.out.println("saving...");
                    for (int i = 0; ; ) {
                        i = in.read();
                        if (i == -1) break;
                        fos.write((char) i);
                    }
                    fos.close();
                    img = loadImage("cap.jpg");
                    if (!fTurning) {
                        mPanoIdPrev = mPanoId;
                    }
                    System.out.println("Prev:" + mPanoIdPrev + "(" + mXidPrev + ")");
                    mPanoIdNext = mPanoId = pano_id;
                    mXidPrev = mXid = xid;
                    fixLinkages();
                    System.out.println("Next:" + mPanoIdNext + "(" + mXid + ")");
                    System.out.println("LinkTo:" + pano_id_link[0] + "," + pano_id_link[1] + "," + pano_id_link[2]);
                }
            } finally {
                if (in != null) try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
