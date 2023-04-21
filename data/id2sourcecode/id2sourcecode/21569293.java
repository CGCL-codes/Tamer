    protected void writeFinalTree(TreeWritterElt l_tempTree, FileInputStream l_tempInStream, DataInputStream l_tempDataInStream, DataOutputStream l_outStream) throws IOException {
        int[] startPositionOffsets = new int[l_tempTree.treeWritterLeafs.length];
        if (0 < startPositionOffsets.length) {
            startPositionOffsets[0] = 0;
            for (int leafIndex = 1; leafIndex < startPositionOffsets.length; leafIndex++) {
                int prevLength = l_tempTree.treeWritterLeafs[leafIndex - 1].length;
                if (-1 == prevLength) {
                    prevLength = 0;
                }
                startPositionOffsets[leafIndex] = startPositionOffsets[leafIndex - 1] + prevLength;
            }
        }
        for (int leafIndex = 0; leafIndex < startPositionOffsets.length; leafIndex++) {
            l_tempInStream.getChannel().position(l_tempTree.treeWritterLeafs[leafIndex].locationInTempFile);
            if (-1 != l_tempTree.treeWritterLeafs[leafIndex].length) {
                l_outStream.writeInt(startPositionOffsets[leafIndex]);
            } else {
                l_outStream.writeInt(-1);
            }
            int numberOfChars = l_tempDataInStream.readInt();
            l_outStream.writeInt(numberOfChars - 4);
            byte[] buffer = new byte[numberOfChars];
            l_tempInStream.read(buffer);
            l_outStream.write(buffer);
        }
        for (int leafIndex = 0; leafIndex < startPositionOffsets.length; leafIndex++) {
            if (-1 != l_tempTree.treeWritterLeafs[leafIndex].length) {
                writeFinalTree(l_tempTree.treeWritterLeafs[leafIndex], l_tempInStream, l_tempDataInStream, l_outStream);
            }
        }
    }
