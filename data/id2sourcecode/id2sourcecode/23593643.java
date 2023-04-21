    private void parse_tWAV(Reader r, int rn) throws IOException {
        r.skip(12);
        String type = r.readString(4);
        boolean cue = type.equals("Cue#");
        boolean adpc = type.equals("ADPC");
        if (cue) {
            int len = r.readInt() + 8;
            println(1, "Cue: (len " + len + ")");
            present(r.read(len - 8));
            r.skip(4);
        }
        if (cue || adpc) {
            int len = r.readInt() + 8;
            println(1, "ADPC: (len " + len + ")");
            present(r.read(len - 8));
            r.skip(4);
        }
        int len = r.readInt();
        short rate = r.readShort();
        int count = r.readInt();
        byte bps = r.readByte();
        byte chan = r.readByte();
        boolean adpcm = r.readShort() == 1;
        println(1, "Sample rate: " + rate);
        println(1, "Sample count: " + count);
        println(1, bps + " bits per sample");
        println(1, chan + " channel" + (chan == 1 ? "" : "s"));
        println(1, (adpcm ? "ADPCM" : "MPEG-2") + " encoded");
        println(1, "Data:");
        present(r.read(0x100));
        println(2, "...");
        ByteArrayOutputStream baos = new ByteArrayOutputStream(len + 44);
        DataOutputStream wav = new DataOutputStream(baos);
        wav.write(new byte[] { 'R', 'I', 'F', 'F' });
        wav.writeInt(len + 36);
        wav.write(new byte[] { 'W', 'A', 'V', 'E', 'f', 'm', 't', ' ' });
        wav.writeInt(16);
        wav.writeShort(1);
        wav.writeShort(chan);
        wav.writeInt(rate);
        wav.writeInt((bps >> 3) * chan);
        wav.writeInt(bps);
        wav.write(new byte[] { 'd', 'a', 't', 'a' });
        wav.writeInt(len);
        wav.write(r.read(len));
        present(baos.toByteArray(), 0, 0x100);
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new ByteArrayInputStream(baos.toByteArray()));
            DataLine.Info dli = new DataLine.Info(Clip.class, ais.getFormat());
            println(1, "line " + dli + (AudioSystem.isLineSupported(dli) ? " is" : " is not") + " supported");
            Clip mus = (Clip) AudioSystem.getLine(dli);
            mus.open(ais);
            mus.loop(1);
            Thread.sleep(mus.getMicrosecondLength() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
