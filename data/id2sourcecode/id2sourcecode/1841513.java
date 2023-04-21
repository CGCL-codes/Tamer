    public int write(Buffer unit, int datlen) {
        if (datlen > unit.readableBytes()) {
            datlen = unit.readableBytes();
        }
        int ret = write(unit.buffer, unit.read_index, datlen);
        if (ret > 0) {
            unit.read_index += ret;
        }
        return ret;
    }
