    int getChannelNumber() {
        int offset = 1;
        return SSHInputStream.getInteger(offset, super._data);
    }
