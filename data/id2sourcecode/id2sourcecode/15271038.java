    public void writeTo(OutputStream out) throws IOException {
        long length = mFile.length();
        int bufSize;
        if (length > 4000) {
            bufSize = 4000;
        } else {
            bufSize = (int) length;
        }
        byte[] inputBuffer = new byte[bufSize];
        mFile.seek(0);
        if (mSurrogates != null) {
            long currentPos = 0;
            int size = mSurrogates.size();
            for (int i = 0; i < size; i++) {
                Surrogate s = (Surrogate) mSurrogates.get(i);
                currentPos = writeTo(inputBuffer, out, currentPos, s.mPos);
                s.mByteData.writeTo(out);
            }
        }
        int readAmount;
        while ((readAmount = mFile.read(inputBuffer, 0, bufSize)) > 0) {
            out.write(inputBuffer, 0, readAmount);
        }
    }
