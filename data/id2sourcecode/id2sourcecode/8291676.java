    public byte ReadUint8() {
        if (d_readIdx + 1 > d_writeIdx) {
            return 0;
        }
        return d_data[d_readIdx++];
    }
