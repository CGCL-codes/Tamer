    protected IDataSet loadDataSet(URL url) throws DataSetException, IOException {
        InputStream in = url.openStream();
        IDataSet ds = new XlsDataSet(in);
        return ds;
    }
