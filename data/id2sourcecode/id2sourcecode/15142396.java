    public boolean isTextChanged() {
        CharSequence text = getText();
        int hash = text.length();
        if (src_text_length != hash) {
            return true;
        }
        byte bytes[] = getString().getBytes();
        mCRC32.update(bytes, 0, bytes.length);
        return src_text_crc32 == mCRC32.getValue();
    }
