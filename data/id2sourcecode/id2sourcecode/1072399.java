    private int getLastMatch() {
        int high = descriptorsSize;
        int low = -1;
        boolean match = false;
        ResourceDescriptor desc = new ResourceDescriptor();
        desc.label = patternString.substring(0, patternString.length() - 1);
        while (high - low > 1) {
            int index = (high + low) / 2;
            String label = descriptors[index].label;
            if (match(label)) {
                low = index;
                match = true;
            } else {
                int compare = descriptors[index].compareTo(desc);
                if (compare == -1) {
                    low = index;
                } else {
                    high = index;
                }
            }
        }
        if (match) {
            return low;
        }
        return -1;
    }
