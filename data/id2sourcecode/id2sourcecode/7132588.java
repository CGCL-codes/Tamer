    public static void main(String[] args) throws Exception {
        DatagramSocket s = new DatagramSocket(8000);
        DatagramPacket packet = new DatagramPacket(new byte[38], 38);
        AudioFormat af1 = new AudioFormat(Encoding.PCM_SIGNED, 11025, 16, 1, 2, 11025, false);
        AudioFormat af2 = new AudioFormat(Encoding.PCM_SIGNED, 11025, 16, 2, 4, 11025, false);
        AudioFormat af3 = new AudioFormat(Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        Line.Info info = new Line.Info(SourceDataLine.class);
        SourceDataLine player = (SourceDataLine) AudioSystem.getLine(info);
        player.open(af3);
        player.start();
        byte[] data = new byte[320];
        byte[] data2 = new byte[2560];
        AudioInputStream ais = AudioSystem.getAudioInputStream(af3, AudioSystem.getAudioInputStream(af2, new AudioInputStream(new ByteArrayInputStream(data), af1, AudioSystem.NOT_SPECIFIED)));
        int decoder = Speex.createDecoder();
        while (true) {
            s.receive(packet);
            byte[] temp = packet.getData();
            System.arraycopy(temp, 0, data, 0, temp.length);
            Speex.decode(decoder, data, data.length);
            ais.reset();
            System.out.print("avail: " + ais.available());
            int read = ais.read(data2);
            System.out.println(", read: " + read);
            player.write(data2, 0, read);
        }
    }
