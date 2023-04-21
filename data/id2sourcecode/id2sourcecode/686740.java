    void processAudioData(byte[] buffer, int index, int bytes_per_sample) {
        byte[] dummybuffer = new byte[100];
        int samples_at_11 = 0;
        switch(rate) {
            case 44100:
                samples_at_11 = 4;
                break;
            case 22050:
                samples_at_11 = 2;
                break;
            case 11025:
                samples_at_11 = 1;
                break;
            default:
                System.out.println("Huh? Here is my rate: " + rate);
        }
        int samplestoskip = samples_at_11 - 1;
        if (channels == 2) {
            samplestoskip += samples_at_11;
        }
        int bytestoskip = samplestoskip * (bits_per_sample / 8);
        System.out.println("Bytes to skip: " + bytestoskip);
        System.out.println("Samples to skip: " + samplestoskip);
        ByteArrayInputStream in = new ByteArrayInputStream(buffer);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (in.available() > 0) {
            baos.write(in.read());
            if (bits_per_sample == 16) baos.write(in.read()); else baos.write(0);
            try {
                if (bytestoskip != 0) {
                    int amount = in.read(dummybuffer, 0, bytestoskip);
                    if (amount != bytestoskip) System.out.println("MASSIVE PROBLEM, could not dummy buffer read: " + in.available() + ", " + amount);
                }
            } catch (Exception e) {
                System.out.println("BAD BAD BAD " + e);
                System.out.println("Length requested: " + bytes_per_sample);
                throw new RuntimeException(e.toString());
            }
        }
        audiodata = baos.toByteArray();
    }
