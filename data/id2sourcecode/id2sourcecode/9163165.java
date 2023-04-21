    @Override
    protected void memcpy(int destination, int source, int length, boolean checkOverlap) {
        if (length <= 0) {
            return;
        }
        destination = normalizeAddress(destination);
        source = normalizeAddress(source);
        if (isIntAligned(source) && isIntAligned(destination) && isIntAligned(length)) {
            memcpyAligned4(destination, source, length, checkOverlap);
        } else if ((source & 0x03) == (destination & 0x03) && (!checkOverlap || !areOverlapping(destination, source, length))) {
            while (!isIntAligned(source) && length > 0) {
                write8(destination, (byte) read8(source));
                source++;
                destination++;
                length--;
            }
            int length4 = length & ~0x03;
            if (length4 > 0) {
                memcpyAligned4(destination, source, length4, checkOverlap);
                source += length4;
                destination += length4;
                length -= length4;
            }
            while (length > 0) {
                write8(destination, (byte) read8(source));
                destination++;
                source++;
                length--;
            }
        } else {
            if (!checkOverlap || source >= destination || !areOverlapping(destination, source, length)) {
                if (areOverlapping(destination, source, 4)) {
                    for (int i = 0; i < length; i++) {
                        write8(destination + i, (byte) read8(source + i));
                    }
                } else {
                    IMemoryReader sourceReader = MemoryReader.getMemoryReader(source, length, 1);
                    for (int i = 0; i < length; i++) {
                        write8(destination + i, (byte) sourceReader.readNext());
                    }
                }
            } else {
                for (int i = length - 1; i >= 0; i--) {
                    write8(destination + i, (byte) read8(source + i));
                }
            }
        }
    }
