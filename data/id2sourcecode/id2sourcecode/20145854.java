    public void Init() {
        Buffer.clear();
        writestate = false;
        initstate = false;
        SimpleSocketChannelHandler hand = (SimpleSocketChannelHandler) getSocketChannelHandler();
        if (hand.State != null) {
            writestate = true;
            SimpleState s = hand.State;
            HoldFile = s.F;
            try {
                FileInputStream fis = new FileInputStream(HoldFile);
                Chan = fis.getChannel();
                Chan.read(Buffer);
                Buffer.flip();
            } catch (IOException e) {
                done = true;
                writestate = false;
                getSocketChannelHandler().Cancel();
                e.printStackTrace();
            }
        } else {
            Buffer.limit(Long.SIZE / Byte.SIZE);
            initstate = true;
        }
        readstate = false;
        done = false;
    }
