    public static void main(String[] args) throws Exception {
        File soundbankFile = new File("/home/peter/NetBeansProjects/frinikaprojects/PJSynthDemoSoundbank/dist/PJSynthDemoSoundbank.jar");
        MidiDevice dev = null;
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            if (info.toString().startsWith("UM1")) {
                dev = MidiSystem.getMidiDevice(info);
                break;
            }
        }
        PJSynth synth = (PJSynth) MidiSystem.getMidiDevice(new PJSynthProvider.PJSynthProviderInfo());
        synth.open();
        synth.loadAllInstruments(MidiSystem.getSoundbank(soundbankFile));
        final TargetDataLine line = (TargetDataLine) ((Mixer) synth).getLine(new Line.Info(TargetDataLine.class));
        AudioFormat.Encoding PCM_FLOAT = new AudioFormat.Encoding("PCM_FLOAT");
        final AudioFormat format = new AudioFormat(PCM_FLOAT, 44100, 32, 2, 4 * 2, 44100, ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN));
        line.open(format);
        dev.open();
        dev.getTransmitter().setReceiver(synth.getReceiver());
        AudioInputStream ais = new AudioInputStream(line);
        final AudioInputStream convertedAis = AudioSystem.getAudioInputStream(Encoding.PCM_SIGNED, ais);
        System.out.println(AudioSystem.getMixerInfo()[0]);
        final SourceDataLine sdl = AudioSystem.getSourceDataLine(convertedAis.getFormat(), AudioSystem.getMixerInfo()[0]);
        sdl.open();
        sdl.start();
        final byte[] buf = new byte[512];
        new Thread() {

            @Override
            public void run() {
                try {
                    long startNanos = System.nanoTime();
                    while (true) {
                        int read = convertedAis.read(buf);
                        sdl.write(buf, 0, read);
                        long waitNanos = ((read / format.getFrameSize())) * 1000000000L / (long) format.getFrameRate();
                        while (System.nanoTime() - startNanos < waitNanos) {
                            if (waitNanos - (System.nanoTime() - startNanos) > 1000000L) Thread.sleep(0, 500000);
                        }
                        startNanos += waitNanos;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        long lastModified = soundbankFile.lastModified();
        while (true) {
            try {
                while (!soundbankFile.exists() || soundbankFile.lastModified() == lastModified) Thread.sleep(500);
                Thread.sleep(500);
                System.out.println("Reloading soundbank");
                Soundbank soundbank = MidiSystem.getSoundbank(soundbankFile);
                System.out.println(soundbank);
                synth.loadAllInstruments(soundbank);
                lastModified = soundbankFile.lastModified();
            } catch (Exception e) {
            }
        }
    }
