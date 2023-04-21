    private void processSystemExclusiveMessage(byte[] data) {
        synchronized (synth.control_mutex) {
            activity();
            if ((data[1] & 0xFF) == 0x7E) {
                int deviceID = data[2] & 0xFF;
                if (deviceID == 0x7F || deviceID == synth.getDeviceID()) {
                    int subid1 = data[3] & 0xFF;
                    int subid2;
                    switch(subid1) {
                        case 0x08:
                            subid2 = data[4] & 0xFF;
                            switch(subid2) {
                                case 0x01:
                                    {
                                        SoftTuning tuning = synth.getTuning(new Patch(0, data[5] & 0xFF));
                                        tuning.load(data);
                                        break;
                                    }
                                case 0x04:
                                case 0x05:
                                case 0x06:
                                case 0x07:
                                    {
                                        SoftTuning tuning = synth.getTuning(new Patch(data[5] & 0xFF, data[6] & 0xFF));
                                        tuning.load(data);
                                        break;
                                    }
                                case 0x08:
                                case 0x09:
                                    {
                                        SoftTuning tuning = new SoftTuning(data);
                                        int channelmask = (data[5] & 0xFF) * 16384 + (data[6] & 0xFF) * 128 + (data[7] & 0xFF);
                                        SoftChannel[] channels = synth.channels;
                                        for (int i = 0; i < channels.length; i++) if ((channelmask & (1 << i)) != 0) channels[i].tuning = tuning;
                                        break;
                                    }
                                default:
                                    break;
                            }
                            break;
                        case 0x09:
                            subid2 = data[4] & 0xFF;
                            switch(subid2) {
                                case 0x01:
                                    synth.setGeneralMidiMode(1);
                                    reset();
                                    break;
                                case 0x02:
                                    synth.setGeneralMidiMode(0);
                                    reset();
                                    break;
                                case 0x03:
                                    synth.setGeneralMidiMode(2);
                                    reset();
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 0x0A:
                            subid2 = data[4] & 0xFF;
                            switch(subid2) {
                                case 0x01:
                                    if (synth.getGeneralMidiMode() == 0) synth.setGeneralMidiMode(1);
                                    synth.voice_allocation_mode = 1;
                                    reset();
                                    break;
                                case 0x02:
                                    synth.setGeneralMidiMode(0);
                                    synth.voice_allocation_mode = 0;
                                    reset();
                                    break;
                                case 0x03:
                                    synth.voice_allocation_mode = 0;
                                    break;
                                case 0x04:
                                    synth.voice_allocation_mode = 1;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            if ((data[1] & 0xFF) == 0x7F) {
                int deviceID = data[2] & 0xFF;
                if (deviceID == 0x7F || deviceID == synth.getDeviceID()) {
                    int subid1 = data[3] & 0xFF;
                    int subid2;
                    switch(subid1) {
                        case 0x04:
                            subid2 = data[4] & 0xFF;
                            switch(subid2) {
                                case 0x01:
                                case 0x02:
                                case 0x03:
                                case 0x04:
                                    int val = (data[5] & 0x7F) + ((data[6] & 0x7F) * 128);
                                    if (subid2 == 0x01) setVolume(val); else if (subid2 == 0x02) setBalance(val); else if (subid2 == 0x03) setFineTuning(val); else if (subid2 == 0x04) setCoarseTuning(val);
                                    break;
                                case 0x05:
                                    int ix = 5;
                                    int slotPathLen = (data[ix++] & 0xFF);
                                    int paramWidth = (data[ix++] & 0xFF);
                                    int valueWidth = (data[ix++] & 0xFF);
                                    int[] slotPath = new int[slotPathLen];
                                    for (int i = 0; i < slotPathLen; i++) {
                                        int msb = (data[ix++] & 0xFF);
                                        int lsb = (data[ix++] & 0xFF);
                                        slotPath[i] = msb * 128 + lsb;
                                    }
                                    int paramCount = (data.length - 1 - ix) / (paramWidth + valueWidth);
                                    long[] params = new long[paramCount];
                                    long[] values = new long[paramCount];
                                    for (int i = 0; i < paramCount; i++) {
                                        values[i] = 0;
                                        for (int j = 0; j < paramWidth; j++) params[i] = params[i] * 128 + (data[ix++] & 0xFF);
                                        for (int j = 0; j < valueWidth; j++) values[i] = values[i] * 128 + (data[ix++] & 0xFF);
                                    }
                                    globalParameterControlChange(slotPath, params, values);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 0x08:
                            subid2 = data[4] & 0xFF;
                            switch(subid2) {
                                case 0x02:
                                    {
                                        SoftTuning tuning = synth.getTuning(new Patch(0, data[5] & 0xFF));
                                        tuning.load(data);
                                        SoftVoice[] voices = synth.getVoices();
                                        for (int i = 0; i < voices.length; i++) if (voices[i].active) if (voices[i].tuning == tuning) voices[i].updateTuning(tuning);
                                        break;
                                    }
                                case 0x07:
                                    {
                                        SoftTuning tuning = synth.getTuning(new Patch(data[5] & 0xFF, data[6] & 0xFF));
                                        tuning.load(data);
                                        SoftVoice[] voices = synth.getVoices();
                                        for (int i = 0; i < voices.length; i++) if (voices[i].active) if (voices[i].tuning == tuning) voices[i].updateTuning(tuning);
                                        break;
                                    }
                                case 0x08:
                                case 0x09:
                                    {
                                        SoftTuning tuning = new SoftTuning(data);
                                        int channelmask = (data[5] & 0xFF) * 16384 + (data[6] & 0xFF) * 128 + (data[7] & 0xFF);
                                        SoftChannel[] channels = synth.channels;
                                        for (int i = 0; i < channels.length; i++) if ((channelmask & (1 << i)) != 0) channels[i].tuning = tuning;
                                        SoftVoice[] voices = synth.getVoices();
                                        for (int i = 0; i < voices.length; i++) if (voices[i].active) if ((channelmask & (1 << (voices[i].channel))) != 0) voices[i].updateTuning(tuning);
                                        break;
                                    }
                                default:
                                    break;
                            }
                            break;
                        case 0x09:
                            subid2 = data[4] & 0xFF;
                            switch(subid2) {
                                case 0x01:
                                    {
                                        int[] destinations = new int[(data.length - 7) / 2];
                                        int[] ranges = new int[(data.length - 7) / 2];
                                        int ix = 0;
                                        for (int j = 6; j < data.length - 1; j += 2) {
                                            destinations[ix] = data[j] & 0xFF;
                                            ranges[ix] = data[j + 1] & 0xFF;
                                            ix++;
                                        }
                                        int channel = data[5] & 0xFF;
                                        SoftChannel softchannel = synth.channels[channel];
                                        softchannel.mapChannelPressureToDestination(destinations, ranges);
                                        break;
                                    }
                                case 0x02:
                                    {
                                        int[] destinations = new int[(data.length - 7) / 2];
                                        int[] ranges = new int[(data.length - 7) / 2];
                                        int ix = 0;
                                        for (int j = 6; j < data.length - 1; j += 2) {
                                            destinations[ix] = data[j] & 0xFF;
                                            ranges[ix] = data[j + 1] & 0xFF;
                                            ix++;
                                        }
                                        int channel = data[5] & 0xFF;
                                        SoftChannel softchannel = synth.channels[channel];
                                        softchannel.mapPolyPressureToDestination(destinations, ranges);
                                        break;
                                    }
                                case 0x03:
                                    {
                                        int[] destinations = new int[(data.length - 7) / 2];
                                        int[] ranges = new int[(data.length - 7) / 2];
                                        int ix = 0;
                                        for (int j = 7; j < data.length - 1; j += 2) {
                                            destinations[ix] = data[j] & 0xFF;
                                            ranges[ix] = data[j + 1] & 0xFF;
                                            ix++;
                                        }
                                        int channel = data[5] & 0xFF;
                                        SoftChannel softchannel = synth.channels[channel];
                                        int control = data[6] & 0xFF;
                                        softchannel.mapControlToDestination(control, destinations, ranges);
                                        break;
                                    }
                                default:
                                    break;
                            }
                            break;
                        case 0x0A:
                            {
                                subid2 = data[4] & 0xFF;
                                switch(subid2) {
                                    case 0x01:
                                        int channel = data[5] & 0xFF;
                                        int keynumber = data[6] & 0xFF;
                                        SoftChannel softchannel = synth.channels[channel];
                                        for (int j = 7; j < data.length - 1; j += 2) {
                                            int controlnumber = data[j] & 0xFF;
                                            int controlvalue = data[j + 1] & 0xFF;
                                            softchannel.controlChangePerNote(keynumber, controlnumber, controlvalue);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            }
                        default:
                            break;
                    }
                }
            }
        }
    }
