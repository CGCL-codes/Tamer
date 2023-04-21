    void setDestinations(int nArgs, FtsAtom[] args) {
        destinations = new String[nArgs / 2];
        destinationChannels = new int[nArgs / 2];
        int j = 0;
        for (int i = 0; i < nArgs; i += 2) {
            destinations[j] = args[i].symbolValue.toString();
            destinationChannels[j] = args[i + 1].intValue;
            j++;
        }
        if (listener != null) listener.destinationsChanged();
    }
