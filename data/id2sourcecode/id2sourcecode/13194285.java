    @Override
    protected int _readChar() throws ConditionThrowable, java.io.IOException {
        int n = in._readChar();
        if (n >= 0) {
            if (unreadChar < 0) out._writeChar((char) n); else unreadChar = -1;
        }
        return n;
    }
