        private static int[][] getChannelsSMAUG(RandomAccessFile theSpecFile, Header aHeader) throws java.io.IOException {
            theSpecFile.seek(SpecFormat_spc.BITMASKRECORD_OFFSET);
            byte[] arrayOfBitmaskRecord = new byte[aHeader.numBitmaskRecord * 1024];
            theSpecFile.readFully(arrayOfBitmaskRecord);
            int[] arrayOfSetBit = SpecFormat.getArrayOfSetBit(arrayOfBitmaskRecord, aHeader.numNonZeroElements);
            theSpecFile.seek((aHeader.numBitmaskRecord + 1) * 1024);
            int[][] arrayOfChannelCount = new int[aHeader.yLength][aHeader.xLength];
            int maxChannelCount = Integer.MIN_VALUE;
            int yptr;
            int xptr;
            for (int i = 0; i < aHeader.numNonZeroElements; i++) {
                yptr = (int) (arrayOfSetBit[i] / aHeader.xLength);
                xptr = (int) (arrayOfSetBit[i] % aHeader.xLength);
                switch(aHeader.loadFormatIndicator) {
                    case 2:
                        arrayOfChannelCount[yptr][xptr] = (int) MathPlus.byteSwap(theSpecFile.readShort());
                        break;
                    case 4:
                        arrayOfChannelCount[yptr][xptr] = (int) MathPlus.byteSwap(theSpecFile.readInt());
                        break;
                    case 1:
                        arrayOfChannelCount[yptr][xptr] = (int) theSpecFile.readByte();
                        break;
                    default:
                        arrayOfChannelCount[yptr][xptr] = (int) MathPlus.byteSwap(theSpecFile.readShort());
                        break;
                }
                maxChannelCount = Math.max(maxChannelCount, arrayOfChannelCount[yptr][xptr]);
            }
            return arrayOfChannelCount;
        }
