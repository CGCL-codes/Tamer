    boolean write(GnuPacket p) {
        if ((writePtr - readPtr) >= size) {
            return false;
        } else {
            packets[writePtr % size] = p;
            writePtr++;
            return true;
        }
    }
