    boolean willSkip() {
        return (writePos - readPos) >= MAX_PACKETS;
    }
