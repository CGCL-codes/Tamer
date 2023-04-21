    protected void computeAxisLengths() throws FormatException {
        int sno = getSeries();
        int[] count = fp.getCount();
        int[] axes = ag[sno].getAxisTypes();
        int numZ = ag[sno].getAxisCountZ();
        int numC = ag[sno].getAxisCountC();
        int numT = ag[sno].getAxisCountT();
        core.sizeZ[sno] = sizeZ[sno];
        core.sizeC[sno] = sizeC[sno];
        core.sizeT[sno] = sizeT[sno];
        lenZ[sno] = new int[numZ + 1];
        lenC[sno] = new int[numC + 1];
        lenT[sno] = new int[numT + 1];
        lenZ[sno][0] = sizeZ[sno];
        lenC[sno][0] = sizeC[sno];
        lenT[sno][0] = sizeT[sno];
        for (int i = 0, z = 1, c = 1, t = 1; i < axes.length; i++) {
            switch(axes[i]) {
                case AxisGuesser.Z_AXIS:
                    core.sizeZ[sno] *= count[i];
                    lenZ[sno][z++] = count[i];
                    break;
                case AxisGuesser.C_AXIS:
                    core.sizeC[sno] *= count[i];
                    lenC[sno][c++] = count[i];
                    break;
                case AxisGuesser.T_AXIS:
                    core.sizeT[sno] *= count[i];
                    lenT[sno][t++] = count[i];
                    break;
                default:
                    throw new FormatException("Unknown axis type for axis #" + i + ": " + axes[i]);
            }
        }
        int[] cLengths = reader.getChannelDimLengths();
        String[] cTypes = reader.getChannelDimTypes();
        int cCount = 0;
        for (int i = 0; i < cLengths.length; i++) {
            if (cLengths[i] > 1) cCount++;
        }
        for (int i = 1; i < lenC[sno].length; i++) {
            if (lenC[sno][i] > 1) cCount++;
        }
        if (cCount == 0) {
            core.cLengths[sno] = new int[] { 1 };
            core.cTypes[sno] = new String[] { FormatTools.CHANNEL };
        } else {
            core.cLengths[sno] = new int[cCount];
            core.cTypes[sno] = new String[cCount];
        }
        int c = 0;
        for (int i = 0; i < cLengths.length; i++) {
            if (cLengths[i] == 1) continue;
            core.cLengths[sno][c] = cLengths[i];
            core.cTypes[sno][c] = cTypes[i];
            c++;
        }
        for (int i = 1; i < lenC[sno].length; i++) {
            if (lenC[sno][i] == 1) continue;
            core.cLengths[sno][c] = lenC[sno][i];
            core.cTypes[sno][c] = FormatTools.CHANNEL;
        }
        int pixelType = getPixelType();
        boolean little = reader.isLittleEndian();
        MetadataStore s = reader.getMetadataStore();
        s.setPixels(new Integer(core.sizeX[sno]), new Integer(core.sizeY[sno]), new Integer(core.sizeZ[sno]), new Integer(core.sizeC[sno]), new Integer(core.sizeT[sno]), new Integer(pixelType), new Boolean(!little), core.currentOrder[sno], new Integer(sno), null);
    }
