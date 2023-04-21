    public void send(MidiMessage msg, long timeStamp) {
        byte[] data = msg.getMessage();
        if (msg instanceof ShortMessage) {
            ShortMessage shortMessage = (ShortMessage) msg;
            switch(shortMessage.getCommand()) {
                case 0xF0:
                    if (shortMessage.getChannel() == 8) {
                        this.configuration.send(msg, timeStamp);
                    }
                    if (shortMessage.getChannel() == 0x0C) {
                        this.configuration.send(msg, timeStamp);
                    }
                    break;
                default:
                    break;
            }
        }
        if (!(msg instanceof SysexMessage)) {
            return;
        }
        if (data[1] == 125) {
            byte[] bytes = { data[2], data[3] };
            float tempo = (float) (this.midiToInt(bytes) / 50.0);
            int overdub = data[4];
            byte[] sceneBytes = { data[5], data[6] };
            int scene_num = this.midiToInt(sceneBytes);
            this.configuration.abletonState.setTempo(tempo);
            this.configuration.abletonState.setOverdub(overdub);
        }
        if (data[1] == 126) {
            int tracknum = 0;
            int clipnum = 0;
            boolean get_clip_num = false;
            boolean get_clip_state = false;
            boolean get_length = false;
            int track_armed = 0;
            int clip_state = 0;
            for (int i = 0; i < data.length; i++) {
                if (data[i] == -16 || data[i] == -9) {
                    continue;
                }
                if (data[i] == 126) {
                    tracknum = data[i + 1];
                    track_armed = data[i + 2];
                    AbletonTrack track = this.configuration.abletonState.getTrack(tracknum);
                    if (track == null) {
                        track = this.configuration.abletonState.createTrack(tracknum);
                    }
                    track.setArm(track_armed);
                    get_clip_num = true;
                    i += 2;
                    continue;
                }
                if (get_clip_num) {
                    get_clip_num = false;
                    byte[] bytes = { data[i], data[i + 1] };
                    i++;
                    clipnum = this.midiToInt(bytes);
                    get_clip_state = true;
                    continue;
                }
                if (get_clip_state) {
                    clip_state = data[i];
                    if (clip_state != 0) {
                        get_clip_state = false;
                        get_length = true;
                    } else {
                        get_clip_state = false;
                        get_clip_num = true;
                        AbletonTrack track = this.configuration.abletonState.getTrack(tracknum);
                        if (track == null) {
                            track = this.configuration.abletonState.createTrack(tracknum);
                        }
                        AbletonClip clip = track.getClip(clipnum);
                        if (clip == null) {
                            clip = track.createClip(clipnum);
                        }
                        clip.setState(clip_state);
                    }
                    continue;
                }
                if (get_length) {
                    get_length = false;
                    byte[] bytes = { data[i], data[i + 1] };
                    float length = (float) ((double) this.midiToInt(bytes) / 100.0);
                    i++;
                    AbletonTrack track = this.configuration.abletonState.getTrack(tracknum);
                    if (track == null) {
                        track = this.configuration.abletonState.createTrack(tracknum);
                    }
                    AbletonClip clip = track.getClip(clipnum);
                    if (clip == null) {
                        clip = track.createClip(clipnum);
                    }
                    clip.setLength(length);
                    get_clip_num = true;
                }
            }
        }
    }
