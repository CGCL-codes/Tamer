    public void setTextFinger() {
        src_text_length = getText().length();
        byte bytes[] = getString().getBytes();
        mCRC32.update(bytes, 0, bytes.length);
        src_text_crc32 = mCRC32.getValue();
    }
