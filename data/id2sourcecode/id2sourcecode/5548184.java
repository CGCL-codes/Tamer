    private void getContextNodeBinarySearch() {
        int low = MIN_CONTEXT_LENGTH;
        int high = _contextLength;
        _contextLength = MIN_CONTEXT_LENGTH - 1;
        _contextNode = null;
        boolean isDeterministic = false;
        while (high >= low) {
            int contextLength = (high + low) / 2;
            PPMNode contextNode = lookupNode(contextLength);
            if (contextNode == null || contextNode.isChildless(_excludedBytes)) {
                if (contextLength < high) high = contextLength; else --high;
            } else if (contextNode.isDeterministic(_excludedBytes)) {
                _contextLength = contextLength;
                _contextNode = contextNode;
                isDeterministic = true;
                if (contextLength < high) high = contextLength; else --high;
            } else if (!isDeterministic) {
                _contextLength = contextLength;
                _contextNode = contextNode;
                if (contextLength > low) low = contextLength; else ++low;
            } else {
                if (contextLength > low) low = contextLength; else ++low;
            }
        }
    }
