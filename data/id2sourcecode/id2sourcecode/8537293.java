    public void fillBuffer() {
        if (line.available() < byteBuffer.length) {
            System.out.println('_');
        } else if (doFlush) {
            doFlush = false;
            line.flush();
            System.out.println(getName() + " flushed");
        } else {
            latencyFrames = (int) (line.getLongFramePosition() - framesRead);
            try {
                int nread = ((TargetDataLine) line).read(byteBuffer, 0, byteBuffer.length);
                framesRead += nread / 2 / af.getChannels();
                if (nread == 0) System.out.println("active :" + line.isActive() + " available:" + line.available() + " nByte: " + byteBuffer.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
