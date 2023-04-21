        private static int[] getChannelsMUSORT(RandomAccessFile theSpecFile, Header aHeader) throws java.io.IOException {
            theSpecFile.seek(SpecFormat_spc.BITMASKRECORD_OFFSET);
            int[] arrayOfChannelCount = new int[aHeader.xLength];
            int maxChannelCount = Integer.MIN_VALUE;
            for (int i = 0; i < aHeader.xLength; i++) {
                switch(aHeader.origLoadFormatIndicator) {
                    case 2:
                        arrayOfChannelCount[i] = (int) MathPlus.byteSwap(theSpecFile.readShort());
                        break;
                    case 4:
                        arrayOfChannelCount[i] = (int) MathPlus.byteSwap(theSpecFile.readInt());
                        break;
                    case 1:
                        arrayOfChannelCount[i] = (int) theSpecFile.readByte();
                        break;
                    default:
                        arrayOfChannelCount[i] = (int) MathPlus.byteSwap(theSpecFile.readShort());
                        break;
                }
                maxChannelCount = Math.max(maxChannelCount, arrayOfChannelCount[i]);
            }
            return arrayOfChannelCount;
        }
