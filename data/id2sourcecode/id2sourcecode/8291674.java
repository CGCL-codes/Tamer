    public int ReadUint32() {
        int ret = 0;
        if (d_readIdx + 4 > d_writeIdx) {
            return 0;
        }
        int tmp;
        tmp = d_data[d_readIdx++];
        ret |= (tmp & 0xFF);
        tmp = d_data[d_readIdx++];
        ret |= ((tmp & 0xFF) << 8);
        tmp = d_data[d_readIdx++];
        ret |= ((tmp & 0xFF) << 16);
        tmp = d_data[d_readIdx++];
        ret |= ((tmp & 0xFF) << 24);
        return ret;
    }
