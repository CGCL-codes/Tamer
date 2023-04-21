    public KaboumFeatureShuttle getServerResponse(KaboumFeatureShuttle shuttleIn) {
        KaboumFeatureShuttle shuttleOut = null;
        ObjectOutputStream oout = null;
        ObjectInputStream oin = null;
        URL serverURL = KaboumUtil.toURL(featureServerURL);
        URLConnection con = null;
        try {
            long t0 = System.currentTimeMillis();
            KaboumUtil.debug("FeatureServer call: " + serverURL.toString());
            con = serverURL.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);
            con.setRequestProperty("Content-Type", "application/octet-stream");
            long tcon = System.currentTimeMillis();
            KaboumUtil.debug("sending shuttle: " + shuttleIn.toString());
            oout = new ObjectOutputStream(con.getOutputStream());
            oout.writeObject(shuttleIn);
            oout.flush();
            oout.close();
            shuttleIn = null;
            long twrite = System.currentTimeMillis();
            oin = new ObjectInputStream(con.getInputStream());
            shuttleOut = (KaboumFeatureShuttle) oin.readObject();
            KaboumUtil.debug("received shuttle: " + shuttleOut.toString());
            long tread = System.currentTimeMillis();
            loadGeoClassesParameters(shuttleOut);
            oin.close();
            long tend = System.currentTimeMillis();
            System.out.println("time taken for entire connection : " + (tend - t0));
            System.out.println("time taken for openConnection    : " + (tcon - t0));
            System.out.println("time taken for sending shuttle   : " + (twrite - tcon));
            System.out.println("time taken for receiving shuttle : " + (tread - twrite));
            System.out.println("time taken for params parsing    : " + (tend - tread));
        } catch (Exception ioe) {
            ioe.printStackTrace();
            KaboumUtil.debug(ioe.getMessage());
        }
        return shuttleOut;
    }
