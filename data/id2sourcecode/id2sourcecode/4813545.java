    protected IRNode getImportedNode(int index) throws IOException {
        int low = 0, high = importOffsets.size();
        while (low < high - 1) {
            int mid = (low + high) / 2;
            if (index <= ((Integer) importOffsets.elementAt(mid)).intValue()) high = mid; else low = mid;
        }
        index -= ((Integer) importOffsets.elementAt(low)).intValue();
        return ((IRRegion) imports.elementAt(low)).getNode(index);
    }
