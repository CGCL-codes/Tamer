    public String ReadString() {
        StringBuffer str = new StringBuffer();
        while (d_readIdx < d_writeIdx && d_data[d_readIdx] != 0) {
            str.append((char) d_data[d_readIdx++]);
        }
        if (d_readIdx < d_writeIdx) {
            d_readIdx++;
        }
        return str.toString();
    }
