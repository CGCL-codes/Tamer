    void removeConnectionByIndex(int index) {
        --nConnected;
        for (int i = index; i < nConnected; ++i) {
            connected[i] = connected[i + 1];
        }
    }
