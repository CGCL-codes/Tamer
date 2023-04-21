    protected void removeDataSource(int i) {
        DataSource ds = (DataSource) get(i);
        if (sourceInfo != null) {
            SourceInfo[] si = new SourceInfo[sourceInfo.length - 1];
            for (int j = 0; j < i; ++j) si[j] = sourceInfo[j];
            for (int j = i; j < si.length; ++j) si[j] = sourceInfo[j + 1];
            sourceInfo = si;
        }
        if (buffers != null) {
            Buffer[] bf = new Buffer[buffers.length - 1];
            for (int j = 0; j < i; ++j) bf[j] = buffers[j];
            for (int j = i; j < bf.length; ++j) bf[j] = buffers[j + 1];
            buffers = bf;
        }
        remove(i);
        for (; i < size(); ++i) {
            Object o = get(i);
            if (o instanceof BufferedDataSource) {
                o = ((BufferedDataSource) o).dataSource;
            }
            if (o instanceof CollectiveDataSource) {
                ((CollectiveDataSource) o).myIndex = i;
            }
        }
        if (ds instanceof BufferedDataSource) {
            ds = ((BufferedDataSource) ds).dataSource;
        }
        if (ds instanceof CollectiveDataSource) {
            ((CollectiveDataSource) ds).myIndex = -1;
        }
        notifyListenersForDataSourceRemoved(ds);
    }
