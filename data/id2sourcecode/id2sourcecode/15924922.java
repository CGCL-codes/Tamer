    public void mostraFormato(AudioFormat formato) {
        System.out.println("Encoding: " + formato.getEncoding().toString());
        System.out.println("channels: " + formato.getChannels());
        System.out.println("frameSize: " + formato.getFrameSize());
        System.out.println("frameRate: " + formato.getFrameRate());
        System.out.println("bigEndian: " + formato.isBigEndian());
        System.out.println("sample Rate: " + formato.getSampleRate());
        System.out.println("sampleSizeInBits: " + formato.getSampleSizeInBits());
    }
