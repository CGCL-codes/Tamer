    private void handlePort(AlsaSeqClientInfo clientInfo, AlsaSeqPortInfo portInfo) {
        int nClient = clientInfo.getClient();
        int nPort = portInfo.getPort();
        int nType = portInfo.getType();
        int nCapability = portInfo.getCapability();
        int nSynthVoices = portInfo.getSynthVoices();
        if (TDebug.TracePortScan) {
            TDebug.out("AlsaMidiDeviceProvider.scanPorts(): port: " + nPort);
            TDebug.out("AlsaMidiDeviceProvider.scanPorts(): type: " + nType);
            TDebug.out("AlsaMidiDeviceProvider.scanPorts(): cap: " + nCapability);
            TDebug.out("AlsaMidiDeviceProvider.scanPorts(): midi channels: " + portInfo.getMidiChannels());
            TDebug.out("AlsaMidiDeviceProvider.scanPorts(): midi voices: " + portInfo.getMidiVoices());
            TDebug.out("AlsaMidiDeviceProvider.scanPorts(): synth voices: " + portInfo.getSynthVoices());
        }
        if ((nType & AlsaSeq.SND_SEQ_PORT_TYPE_MIDI_GENERIC) != 0) {
            MidiDevice device = null;
            if ((nType & (AlsaSeq.SND_SEQ_PORT_TYPE_SYNTH | AlsaSeq.SND_SEQ_PORT_TYPE_DIRECT_SAMPLE | AlsaSeq.SND_SEQ_PORT_TYPE_SAMPLE)) != 0) {
                boolean bWriteSubscriptionAllowed = (nCapability & WRITE_CAPABILITY) == WRITE_CAPABILITY;
                if (bWriteSubscriptionAllowed) {
                    device = new AlsaSynthesizer(nClient, nPort, nSynthVoices);
                } else {
                    if (TDebug.TraceMidiDeviceProvider) {
                        TDebug.out("AlsaMidiDeviceProvider.getDevice(): port does not allows write subscription, not used");
                    }
                }
            } else {
                boolean bReadSubscriptionAllowed = (nCapability & READ_CAPABILITY) == READ_CAPABILITY;
                boolean bWriteSubscriptionAllowed = (nCapability & WRITE_CAPABILITY) == WRITE_CAPABILITY;
                if (bReadSubscriptionAllowed || bWriteSubscriptionAllowed) {
                    device = new AlsaMidiDevice(nClient, nPort, bReadSubscriptionAllowed, bWriteSubscriptionAllowed);
                } else {
                    if (TDebug.TraceMidiDeviceProvider) {
                        TDebug.out("AlsaMidiDeviceProvider.getDevice(): port allows neither read nor write subscription, not used");
                    }
                }
            }
            if (device != null) {
                m_devices.add(device);
            }
        }
    }
