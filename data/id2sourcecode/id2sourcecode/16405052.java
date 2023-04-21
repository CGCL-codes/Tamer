    void setSources(int nArgs, FtsAtom[] args) {
        sources = new String[nArgs / 2];
        sourceChannels = new int[nArgs / 2];
        int j = 0;
        for (int i = 0; i < nArgs; i += 2) {
            sources[j] = args[i].symbolValue.toString();
            sourceChannels[j] = args[i + 1].intValue;
            j++;
        }
        if (listener != null) listener.sourcesChanged();
    }
