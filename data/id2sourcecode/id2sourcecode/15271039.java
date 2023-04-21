    private long writeTo(byte[] inputBuffer, OutputStream out, long fromPos, long toPos) throws IOException {
        if (toPos == fromPos) {
            return fromPos;
        }
        int bufSize = inputBuffer.length;
        int readAmount;
        while (toPos > fromPos) {
            int amount;
            if (bufSize <= (toPos - fromPos)) {
                amount = bufSize;
            } else {
                amount = (int) (toPos - fromPos);
            }
            while ((readAmount = mFile.read(inputBuffer, 0, amount)) > 0) {
                out.write(inputBuffer, 0, readAmount);
                fromPos += readAmount;
                amount -= readAmount;
                if (amount <= 0) {
                    break;
                }
            }
            if (readAmount <= 0) {
                break;
            }
        }
        return fromPos;
    }
