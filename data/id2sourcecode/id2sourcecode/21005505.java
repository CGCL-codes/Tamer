    public static void main(String... args) throws Exception {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        TreeMap<String, MidiDevice> inputDevices = new TreeMap<String, MidiDevice>();
        TreeMap<String, MidiDevice> outputDevices = new TreeMap<String, MidiDevice>();
        for (MidiDevice.Info info : infos) {
            try {
                MidiDevice device = MidiSystem.getMidiDevice(info);
                if ((device.getMaxTransmitters() != 0) && !(device instanceof Synthesizer) && !(device instanceof Sequencer)) {
                    inputDevices.put(info.getName(), device);
                }
                if ((device.getMaxReceivers() != 0) && !(device instanceof Synthesizer) && !(device instanceof Sequencer)) {
                    outputDevices.put(info.getName(), device);
                }
            } catch (MidiUnavailableException e) {
            }
        }
        WireProvider wireProvider = new WireProvider();
        MidiDevice.Info[] wireInfos = wireProvider.getDeviceInfo();
        TreeMap<String, MidiDevice> wireDevices = new TreeMap<String, MidiDevice>();
        for (MidiDevice.Info info : wireInfos) {
            wireDevices.put(info.getName(), wireProvider.getDevice(info));
        }
        MidiDevice inputDevice = inputDevices.get("mLAN MIDI In (2)");
        inputDevice.open();
        Transmitter transmitter = inputDevice.getTransmitter();
        transmitter.setReceiver(new TestReceiver());
        MidiDevice outputDevice = wireDevices.get("Wire: mLAN MIDI Out (2)");
        outputDevice.open();
        Receiver receiver = outputDevice.getReceiver();
        try {
            ByteBuffer buffer = null;
            FileInputStream stream = new FileInputStream("C:/Fichiers/Gt8/sysex/proof.syx");
            FileChannel channel = stream.getChannel();
            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            List<byte[]> messages = new ArrayList<byte[]>();
            try {
                int index = 0;
                while (buffer.hasRemaining()) {
                    if (buffer.get(index) == ((byte) SysexMessage.SYSTEM_EXCLUSIVE)) {
                        boolean f7Found = false;
                        index++;
                        int frameSize = 1;
                        while (!f7Found && buffer.hasRemaining()) {
                            if (buffer.get(index) == ((byte) SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE)) {
                                f7Found = true;
                            }
                            frameSize++;
                            index++;
                        }
                        if (f7Found) {
                            byte[] frameData = new byte[frameSize];
                            buffer.get(frameData);
                            messages.add(frameData);
                        } else {
                            throw new IllegalArgumentException("Missing F7 byte at end of frame.");
                        }
                    } else {
                        throw new IllegalArgumentException("Missing F0 byte at start of frame.");
                    }
                }
            } finally {
                stream.close();
                channel.close();
            }
            for (byte[] message : messages) {
                SysexMessage sysexMessage = new SysexMessage();
                sysexMessage.setMessage(message, message.length);
                receiver.send(sysexMessage, -1);
            }
            Thread.sleep(1000);
            for (int messageIndex = 0; messageIndex < messages.size(); messageIndex++) {
                byte[] message = messages.get(messageIndex);
                byte[] received = receivedFrames.get(messageIndex);
                System.out.print("Checking frame " + messageIndex + ":");
                boolean error = false;
                if (message.length != received.length) {
                    error = true;
                }
                for (int byteIndex = 0; !error && byteIndex < message.length; byteIndex++) {
                    if (message[byteIndex] != received[byteIndex]) {
                        error = true;
                    }
                }
                if (error) {
                    System.out.print("KO.\nReceived:");
                    for (byte currentByte : received) {
                        System.out.print(Integer.toHexString(currentByte) + " ");
                    }
                    System.out.print("\nExpected:");
                    for (byte currentByte : message) {
                        System.out.print(Integer.toHexString(currentByte) + " ");
                    }
                    System.out.print("\n");
                } else {
                    System.out.print("OK.\n");
                }
            }
        } finally {
            transmitter.close();
            receiver.close();
            outputDevice.close();
            inputDevice.close();
        }
    }
