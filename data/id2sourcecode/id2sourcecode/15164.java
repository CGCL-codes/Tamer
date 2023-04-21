    public void setData(byte[] newData) {
        _crc32Calculator.reset();
        _crc32Calculator.update(newData);
        _uccrc32 = (int) _crc32Calculator.getValue();
        _uclength = newData.length;
        _data = compress(newData);
    }
