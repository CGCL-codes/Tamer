    public void print(String str) {
        if (closed) throw new IllegalStateException("writer already closed");
        if (str != null) {
            int fragmentLen = str.length();
            if (fragmentLen > 0) {
                ensureCapacity(count + 1);
                buf[count++] = str;
                len += fragmentLen;
            }
        }
    }
