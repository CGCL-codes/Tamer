    public short ReadUint16() {
        short ret = 0;
        if (d_readIdx + 2 > d_writeIdx) {
            return 0;
        }
        short tmp;
        tmp = d_data[d_readIdx++];
        ret |= (tmp & 0xFF);
        tmp = d_data[d_readIdx++];
        ret |= ((tmp & 0xFF) << 8);
        return ret;
    }
