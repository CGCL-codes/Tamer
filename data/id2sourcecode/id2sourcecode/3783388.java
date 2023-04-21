    public static void main(String args[]) {
        try {
            final AudioServer audioServer = new MultiIOJavaSoundAudioServer();
            List<String> list = audioServer.getAvailableOutputNames();
            Object a[] = new Object[list.size()];
            a = list.toArray(a);
            Object selectedValue = JOptionPane.showInputDialog(null, "Select audio output", "Please", JOptionPane.INFORMATION_MESSAGE, null, a, a[0]);
            final IOAudioProcess outProcess = audioServer.openAudioOutput((String) selectedValue, "output");
            audioServer.start();
            PluckedSynthControls controls = new FourStringBassGuitarControls();
            final PluckedSynth synth = new PluckedSynth(controls);
            synth.open();
            CompoundControlPanel panel = new CompoundControlPanel(controls, 1, null, new ControlPanelFactory() {

                @Override
                protected boolean canEdit() {
                    return true;
                }
            }, true, true);
            JFrame frame = new JFrame();
            frame.setContentPane(panel);
            frame.pack();
            frame.setVisible(true);
            SynthChannel chan = synth.getChannel(0);
            final AudioBuffer buff = audioServer.createAudioBuffer("BUFF");
            buff.setRealTime(true);
            AudioClient client = new AudioClient() {

                public void setEnabled(boolean arg0) {
                }

                public void work(int arg0) {
                    synth.processAudio(buff);
                    buff.setChannelFormat(ChannelFormat.STEREO);
                    outProcess.processAudio(buff);
                }
            };
            audioServer.setClient(client);
            MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
            MidiDevice dev = null;
            for (int i = 0; i < infos.length; i++) {
                System.out.println(infos[i].getName());
                if (infos[i].getName().equals("Virtual Piano")) {
                    MidiDevice.Info rtinfo = infos[i];
                    dev = MidiSystem.getMidiDevice(rtinfo);
                    if (dev.getMaxTransmitters() != 0) {
                        break;
                    }
                    dev = null;
                }
            }
            if (dev == null) {
                return;
            }
            dev.open();
            dev.getTransmitter().setReceiver(new Receiver() {

                public void send(MidiMessage message, long timeStamp) {
                    System.out.println(" HELLO ");
                    synth.transport(message, timeStamp);
                }

                public void close() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(TootMidiTestSimple.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
