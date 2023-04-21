    @Test
    public void testOpenMidiDevice() throws Exception {
        MidiDevice dev = null;
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            if (info.toString().startsWith("UM1")) {
                dev = MidiSystem.getMidiDevice(info);
                break;
            }
        }
        PJSynth synth = (PJSynth) MidiSystem.getMidiDevice(new PJSynthProvider.PJSynthProviderInfo());
        final TargetDataLine line = (TargetDataLine) ((Mixer) synth).getLine(new Line.Info(TargetDataLine.class));
        AudioFormat.Encoding PCM_FLOAT = new AudioFormat.Encoding("PCM_FLOAT");
        AudioFormat format = new AudioFormat(PCM_FLOAT, 44100, 32, 2, 4 * 2, 44100, ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN));
        line.open(format);
        dev.open();
        dev.getTransmitter().setReceiver(synth.getReceiver());
        AudioInputStream ais = new AudioInputStream(line);
        assertTrue(AudioSystem.isConversionSupported(Encoding.PCM_SIGNED, ais.getFormat()));
        AudioInputStream convertedAis = AudioSystem.getAudioInputStream(Encoding.PCM_SIGNED, ais);
        System.out.println(AudioSystem.getMixerInfo()[2]);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(convertedAis.getFormat(), AudioSystem.getMixerInfo()[2]);
        sdl.open();
        sdl.start();
        byte[] buf = new byte[512];
        for (int n = 0; n < 20000; n++) {
            int read = convertedAis.read(buf);
            sdl.write(buf, 0, read);
        }
    }
