    final void transferFrom(final InputStream in, final int length) throws IOException {
        CheckArg.notNegative(length, "length");
        for (int transferred = 0; transferred < length; ) {
            final ByteBuffer buffer = getPacket();
            final int maxStep = Math.min(buffer.remaining(), length - transferred);
            final int pos = buffer.position();
            final int step = in.read(buffer.array(), pos, maxStep);
            if (step > maxStep) {
                throw new IOException("the source stream reported more bytes than requested:" + "\nrequested    = " + maxStep + "\nreturned     = " + step + "\nstream class = " + in.getClass().getName());
            } else if (step <= 0) {
                throw new EOFException("data in source stream less than specified length:" + "\nexpected     = " + length + "\nactual       = " + transferred + "\nstream class = " + in.getClass().getName());
            } else {
                buffer.position(pos + step);
                transferred += step;
            }
        }
    }
