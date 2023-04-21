        private static int[][] getChannelsMUSORT(RandomAccessFile theSpecFile, Header aHeader) throws java.io.IOException {
            theSpecFile.seek(SpecFormat_spc.BITMASKRECORD_OFFSET);
            int[][] arrayOfChannelCount = new int[aHeader.xLength][aHeader.yLength];
            int maxChannelCount = Integer.MIN_VALUE;
            for (int i = 0; i < aHeader.yLength; i++) {
                for (int j = 0; j < aHeader.xLength; j++) {
                    switch(aHeader.origLoadFormatIndicator) {
                        case 2:
                            arrayOfChannelCount[i][j] = (int) MathPlus.byteSwap(theSpecFile.readShort());
                            break;
                        case 4:
                            arrayOfChannelCount[i][j] = (int) MathPlus.byteSwap(theSpecFile.readInt());
                            break;
                        case 1:
                            arrayOfChannelCount[i][j] = (int) theSpecFile.readByte();
                            break;
                        default:
                            arrayOfChannelCount[i][j] = (int) MathPlus.byteSwap(theSpecFile.readShort());
                            break;
                    }
                    maxChannelCount = Math.max(maxChannelCount, arrayOfChannelCount[i][j]);
                }
            }
            return arrayOfChannelCount;
        }
