    private void readControlBytes() throws DataNotValidIOException, IOException {
        int togo = (allRead ? 1 : ds + 1);
        pos = (stripControls ? 0 : 0 - togo);
        int b = 0;
        while (togo > 0) {
            b = super.read();
            if (b == -1) throw new EOFException("EOF while reading control bytes");
            controlBuf[controlBuf.length - togo] = (byte) b;
            if (togo != 1) ctx.update((byte) b);
            --togo;
        }
        if (b != Presentation.CB_OK || !Util.byteArrayEqual(ctx.digest(), expectedHash)) {
            dnv = new DataNotValidIOException(b == Presentation.CB_OK ? Presentation.CB_BAD_DATA : b);
            throw dnv;
        }
        System.arraycopy(controlBuf, 0, expectedHash, 0, ds);
    }
