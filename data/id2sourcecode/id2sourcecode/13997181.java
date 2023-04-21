    public void scan(Address addr) {
        int index = subspace.getIndex(addr);
        int length = ((LargeObjectSpace) mmtkSpace).getSize(addr).toInt();
        if (DEBUG) {
            Log.write("TreadmillDriver: super=", addr);
            Log.write(", index=", index);
            Log.write(", pages=", length);
            Log.write(", bytes=", Conversions.pagesToBytes(length).toInt());
            Log.writeln(", max=", usedSpaceStream.getMaxValue());
        }
        totalObjects++;
        totalUsedSpace += length;
        objectsStream.increment(index, (short) 1);
        int remainder = subspace.spaceRemaining(addr);
        usedSpaceStream.distribute(index, remainder, blockSize, length);
        Address tmp = addr.plus(length);
        if (tmp.GT(maxAddr)) maxAddr = tmp;
    }
