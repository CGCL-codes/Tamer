    private void initHeader(ImageOutputStream ios) throws IOException {
        ios.writeBits(76, 8);
        ios.writeBits(83, 8);
        ios.writeBits(colorSpace.getCodifica(), 8);
        ios.writeBits(h, 16);
        ios.writeBits(w, 16);
        int channelsNumber = colorSpace.getChannelsNumber();
        for (int i = 1; i <= channelsNumber; i++) {
            LSQuantizer.write(colorSpace.getQuantizationMatrix(i, quality), ios);
        }
        for (int i = 1; i <= channelsNumber; i++) {
            CHDEntry.writeCHDDCTable(colorSpace.getDCCHDTable(i), colorSpace.getDCMinCodeLen(i), ios);
        }
        for (int i = 1; i <= channelsNumber; i++) {
            CHDEntry.writeCHDACTable(colorSpace.getACCHDTable(i), colorSpace.getACMinCodeLen(i), ios);
        }
    }
