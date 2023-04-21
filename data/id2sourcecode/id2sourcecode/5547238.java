    public void addRow(IMyVector<Double> row) throws Exception {
        if (_isReadonly) {
            throw new Exception("The matrix is for reading. Not allowed to write");
        }
        for (int i : row.getKeys()) {
            _valuesFile.writeDouble((Double) row.get(i));
            _colsFile.writeInt(i);
        }
        _writeposition += row.length();
        _rowsFile.writeInt(_writeposition);
        m_height++;
        m_width++;
    }
