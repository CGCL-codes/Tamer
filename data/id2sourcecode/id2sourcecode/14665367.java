    void removeConnection(PolyNode fromMe) {
        boolean isFound = false;
        int foundIndex = -1;
        for (int i = 0; i < nConnected; ++i) {
            if (fromMe == connected[i]) {
                isFound = true;
                foundIndex = i;
                break;
            }
        }
        assert (isFound);
        --nConnected;
        for (int i = foundIndex; i < nConnected; ++i) {
            connected[i] = connected[i + 1];
        }
    }
