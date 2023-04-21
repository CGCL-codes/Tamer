    protected TreeWritterElt writeTempTree(SerializableLeaf l_root, DataOutputStream l_dataOut, FileOutputStream l_outStream) throws IOException {
        TreeWritterElt foldElt = new TreeWritterElt();
        foldElt.locationInTempFile = (int) l_outStream.getChannel().position();
        SerializableLeaf[] children = null;
        if (l_root.isBranch()) {
            children = l_root.getChildren();
            if (null == children) {
                children = new SerializableLeaf[0];
            }
        }
        byte[] leafValue = l_root.getLeafValue();
        foldElt.eltLength = 4 + leafValue.length;
        l_dataOut.writeInt(foldElt.eltLength);
        l_dataOut.writeInt(l_root.isBranch() ? children.length : -1);
        l_dataOut.write(leafValue);
        l_dataOut.writeByte('\n');
        if (l_root.isBranch()) {
            foldElt.treeWritterLeafs = new TreeWritterElt[children.length];
            for (int leafIndex = 0; leafIndex < children.length; leafIndex++) {
                foldElt.treeWritterLeafs[leafIndex] = writeTempTree(children[leafIndex], l_dataOut, l_outStream);
                foldElt.length += +foldElt.treeWritterLeafs[leafIndex].eltLength + 4 + 4;
                if (-1 != foldElt.treeWritterLeafs[leafIndex].length) {
                    foldElt.length += foldElt.treeWritterLeafs[leafIndex].length;
                }
            }
        } else {
            foldElt.length = -1;
        }
        return foldElt;
    }
