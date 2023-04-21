    public void fillBuffers(int nFrame) {
        try {
            int nByte = af.getChannels() * nFrame * 2;
            if (inBuffer == null || inBuffer.length != nByte) inBuffer = new byte[nByte];
            if (line.available() >= nByte && nByte > 0) {
                int nread;
                int cnt = 0;
                do {
                    nread = line.read(inBuffer, 0, nByte);
                    if (nread == 0) System.out.println("active :" + line.isActive() + " available:" + line.available() + " nByte: " + nByte + " inBuffersize: " + inBuffer.length);
                    cnt++;
                    for (int n = 0; n < nByte / 2; n++) {
                        short sample = (short) ((0xff & inBuffer[2 * n + 1]) + ((0xff & inBuffer[2 * n]) * 256));
                        float val = sample / 32768f;
                    }
                } while (line.available() > 2 * nByte);
                if (cnt != 1) System.out.println(" COUNT WAS " + cnt);
            } else {
                System.err.println(String.format(" GLITCH avail=%d actual=%d ", line.available(), nByte));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
