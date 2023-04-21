    public void index() throws IOException {
        _writer = null;
        try {
            _writer = new IndexWriter(_index, getAnalyzer(), MaxFieldLength.UNLIMITED);
            MyDataHandler handler = new MyDataHandler(_writer);
            _digester.digest(handler);
            _writer.optimize();
        } finally {
            if (_writer != null) {
                _writer.close();
            }
        }
    }
