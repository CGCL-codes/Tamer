    public void run() {
        int numRead = 0;
        byte[] buf = new byte[playLine.getBufferSize()];
        try {
            while ((numRead = rawData.read(buf, 0, buf.length)) >= 0) playLine.write(buf, 0, numRead);
        } catch (Exception e) {
            e.printStackTrace();
        }
        playLine.drain();
        stopLine();
    }
