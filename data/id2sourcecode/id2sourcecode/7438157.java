    public static QDataSet getDataSet(String spec, InputStream in, ProgressMonitor mon) throws Exception {
        logger.log(Level.FINE, "getDataSet({0},InputStream)", new Object[] { spec });
        String[] ss = spec.split(":", -2);
        String ext;
        int i = ss[0].indexOf("+");
        ext = (i == -1) ? ss[0] : ss[0].substring(i + 1);
        File f = File.createTempFile("autoplot", "." + ext);
        ReadableByteChannel chin = Channels.newChannel(in);
        WritableByteChannel chout = new FileOutputStream(f).getChannel();
        DataSourceUtil.transfer(chin, chout);
        String virtUrl = ss[0] + ":" + f.toURI().toString() + ss[1];
        QDataSet ds = getDataSet(virtUrl, mon);
        return ds;
    }
