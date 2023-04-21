    public static void main(String[] args) throws Exception {
        ReadSound rs = new ReadSound("voz.wav");
        rs.fillBuffer();
        int n = 0;
        Vector buffer = rs.getChannel(1);
        byte[] temp = new byte[buffer.size()];
        for (Iterator i = buffer.iterator(); i.hasNext(); n++) {
            Byte data = (Byte) i.next();
            temp[n] = data;
            System.out.println(temp[n]);
        }
        Receiver r = new Receiver(temp, 100, 10, 20);
        r.generateLoss(3);
        r.generateLoss(4);
        r.generateLoss(5);
        r.generateLoss(6);
        r.generateLoss(7);
        r.getPacket(1);
        r.getPacket(2);
        r.getPacket(3);
        r.getPacket(4);
        r.getPacket(5);
        r.getPacket(6);
        r.getPacket(7);
        byte[] data = new byte[r.getLenghtPacket()];
        r.getPacket(3).getpayload(data);
        byte[] b = r.getBufferBytes();
        player(b, rs.getAudioFormat());
    }
