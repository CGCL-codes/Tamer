    private boolean checkAndReadData() {
        Object read;
        for (int i = 0; i < toRead.length; i++) {
            read = toRead[i].read();
            if (writer.value == null) {
                if (read != null) {
                    return false;
                }
            } else {
                if (!writer.value.equals(read)) {
                    return false;
                }
            }
        }
        return true;
    }
