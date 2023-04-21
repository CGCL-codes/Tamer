    protected void processAudioBuffers() {
        if (synth.weakstream != null && synth.weakstream.silent_samples != 0) {
            sample_pos += synth.weakstream.silent_samples;
            synth.weakstream.silent_samples = 0;
        }
        for (int i = 0; i < buffers.length; i++) {
            if (i != CHANNEL_DELAY_LEFT && i != CHANNEL_DELAY_RIGHT && i != CHANNEL_DELAY_MONO && i != CHANNEL_DELAY_EFFECT1 && i != CHANNEL_DELAY_EFFECT2) buffers[i].clear();
        }
        if (!buffers[CHANNEL_DELAY_LEFT].isSilent()) {
            buffers[CHANNEL_LEFT].swap(buffers[CHANNEL_DELAY_LEFT]);
        }
        if (!buffers[CHANNEL_DELAY_RIGHT].isSilent()) {
            buffers[CHANNEL_RIGHT].swap(buffers[CHANNEL_DELAY_RIGHT]);
        }
        if (!buffers[CHANNEL_DELAY_MONO].isSilent()) {
            buffers[CHANNEL_MONO].swap(buffers[CHANNEL_DELAY_MONO]);
        }
        if (!buffers[CHANNEL_DELAY_EFFECT1].isSilent()) {
            buffers[CHANNEL_EFFECT1].swap(buffers[CHANNEL_DELAY_EFFECT1]);
        }
        if (!buffers[CHANNEL_DELAY_EFFECT2].isSilent()) {
            buffers[CHANNEL_EFFECT2].swap(buffers[CHANNEL_DELAY_EFFECT2]);
        }
        double volume_left;
        double volume_right;
        SoftChannelMixerContainer[] act_registeredMixers;
        synchronized (control_mutex) {
            long msec_pos = (long) (sample_pos * (1000000.0 / samplerate));
            processMessages(msec_pos);
            if (active_sensing_on) {
                if ((msec_pos - msec_last_activity) > 1000000) {
                    active_sensing_on = false;
                    for (SoftChannel c : synth.channels) c.allSoundOff();
                }
            }
            for (int i = 0; i < voicestatus.length; i++) if (voicestatus[i].active) voicestatus[i].processControlLogic();
            sample_pos += buffer_len;
            double volume = co_master_volume[0];
            volume_left = volume;
            volume_right = volume;
            double balance = co_master_balance[0];
            if (balance > 0.5) volume_left *= (1 - balance) * 2; else volume_right *= balance * 2;
            chorus.processControlLogic();
            reverb.processControlLogic();
            agc.processControlLogic();
            if (cur_registeredMixers == null) {
                if (registeredMixers != null) {
                    cur_registeredMixers = new SoftChannelMixerContainer[registeredMixers.size()];
                    registeredMixers.toArray(cur_registeredMixers);
                }
            }
            act_registeredMixers = cur_registeredMixers;
            if (act_registeredMixers != null) if (act_registeredMixers.length == 0) act_registeredMixers = null;
        }
        if (act_registeredMixers != null) {
            SoftAudioBuffer leftbak = buffers[CHANNEL_LEFT];
            SoftAudioBuffer rightbak = buffers[CHANNEL_RIGHT];
            SoftAudioBuffer monobak = buffers[CHANNEL_MONO];
            SoftAudioBuffer delayleftbak = buffers[CHANNEL_DELAY_LEFT];
            SoftAudioBuffer delayrightbak = buffers[CHANNEL_DELAY_RIGHT];
            SoftAudioBuffer delaymonobak = buffers[CHANNEL_DELAY_MONO];
            int bufferlen = buffers[CHANNEL_LEFT].getSize();
            float[][] cbuffer = new float[nrofchannels][];
            float[][] obuffer = new float[nrofchannels][];
            obuffer[0] = leftbak.array();
            if (nrofchannels != 1) obuffer[1] = rightbak.array();
            for (SoftChannelMixerContainer cmixer : act_registeredMixers) {
                buffers[CHANNEL_LEFT] = cmixer.buffers[CHANNEL_LEFT];
                buffers[CHANNEL_RIGHT] = cmixer.buffers[CHANNEL_RIGHT];
                buffers[CHANNEL_MONO] = cmixer.buffers[CHANNEL_MONO];
                buffers[CHANNEL_DELAY_LEFT] = cmixer.buffers[CHANNEL_DELAY_LEFT];
                buffers[CHANNEL_DELAY_RIGHT] = cmixer.buffers[CHANNEL_DELAY_RIGHT];
                buffers[CHANNEL_DELAY_MONO] = cmixer.buffers[CHANNEL_DELAY_MONO];
                buffers[CHANNEL_LEFT].clear();
                buffers[CHANNEL_RIGHT].clear();
                buffers[CHANNEL_MONO].clear();
                if (!buffers[CHANNEL_DELAY_LEFT].isSilent()) {
                    buffers[CHANNEL_LEFT].swap(buffers[CHANNEL_DELAY_LEFT]);
                }
                if (!buffers[CHANNEL_DELAY_RIGHT].isSilent()) {
                    buffers[CHANNEL_RIGHT].swap(buffers[CHANNEL_DELAY_RIGHT]);
                }
                if (!buffers[CHANNEL_DELAY_MONO].isSilent()) {
                    buffers[CHANNEL_MONO].swap(buffers[CHANNEL_DELAY_MONO]);
                }
                cbuffer[0] = buffers[CHANNEL_LEFT].array();
                if (nrofchannels != 1) cbuffer[1] = buffers[CHANNEL_RIGHT].array();
                boolean hasactivevoices = false;
                for (int i = 0; i < voicestatus.length; i++) if (voicestatus[i].active) if (voicestatus[i].channelmixer == cmixer.mixer) {
                    voicestatus[i].processAudioLogic(buffers);
                    hasactivevoices = true;
                }
                if (!buffers[CHANNEL_MONO].isSilent()) {
                    float[] mono = buffers[CHANNEL_MONO].array();
                    float[] left = buffers[CHANNEL_LEFT].array();
                    if (nrofchannels != 1) {
                        float[] right = buffers[CHANNEL_RIGHT].array();
                        for (int i = 0; i < bufferlen; i++) {
                            float v = mono[i];
                            left[i] += v;
                            right[i] += v;
                        }
                    } else {
                        for (int i = 0; i < bufferlen; i++) {
                            left[i] += mono[i];
                        }
                    }
                }
                if (!cmixer.mixer.process(cbuffer, 0, bufferlen)) {
                    synchronized (control_mutex) {
                        registeredMixers.remove(cmixer);
                        cur_registeredMixers = null;
                    }
                }
                for (int i = 0; i < cbuffer.length; i++) {
                    float[] cbuff = cbuffer[i];
                    float[] obuff = obuffer[i];
                    for (int j = 0; j < bufferlen; j++) obuff[j] += cbuff[j];
                }
                if (!hasactivevoices) {
                    synchronized (control_mutex) {
                        if (stoppedMixers != null) {
                            if (stoppedMixers.contains(cmixer)) {
                                stoppedMixers.remove(cmixer);
                                cmixer.mixer.stop();
                            }
                        }
                    }
                }
            }
            buffers[CHANNEL_LEFT] = leftbak;
            buffers[CHANNEL_RIGHT] = rightbak;
            buffers[CHANNEL_MONO] = monobak;
            buffers[CHANNEL_DELAY_LEFT] = delayleftbak;
            buffers[CHANNEL_DELAY_RIGHT] = delayrightbak;
            buffers[CHANNEL_DELAY_MONO] = delaymonobak;
        }
        for (int i = 0; i < voicestatus.length; i++) if (voicestatus[i].active) if (voicestatus[i].channelmixer == null) voicestatus[i].processAudioLogic(buffers);
        if (!buffers[CHANNEL_MONO].isSilent()) {
            float[] mono = buffers[CHANNEL_MONO].array();
            float[] left = buffers[CHANNEL_LEFT].array();
            int bufferlen = buffers[CHANNEL_LEFT].getSize();
            if (nrofchannels != 1) {
                float[] right = buffers[CHANNEL_RIGHT].array();
                for (int i = 0; i < bufferlen; i++) {
                    float v = mono[i];
                    left[i] += v;
                    right[i] += v;
                }
            } else {
                for (int i = 0; i < bufferlen; i++) {
                    left[i] += mono[i];
                }
            }
        }
        if (synth.chorus_on) chorus.processAudio();
        if (synth.reverb_on) reverb.processAudio();
        if (nrofchannels == 1) volume_left = (volume_left + volume_right) / 2;
        if (last_volume_left != volume_left || last_volume_right != volume_right) {
            float[] left = buffers[CHANNEL_LEFT].array();
            float[] right = buffers[CHANNEL_RIGHT].array();
            int bufferlen = buffers[CHANNEL_LEFT].getSize();
            float amp;
            float amp_delta;
            amp = (float) (last_volume_left * last_volume_left);
            amp_delta = (float) ((volume_left * volume_left - amp) / bufferlen);
            for (int i = 0; i < bufferlen; i++) {
                amp += amp_delta;
                left[i] *= amp;
            }
            if (nrofchannels != 1) {
                amp = (float) (last_volume_right * last_volume_right);
                amp_delta = (float) ((volume_right * volume_right - amp) / bufferlen);
                for (int i = 0; i < bufferlen; i++) {
                    amp += amp_delta;
                    right[i] *= volume_right;
                }
            }
            last_volume_left = volume_left;
            last_volume_right = volume_right;
        } else {
            if (volume_left != 1.0 || volume_right != 1.0) {
                float[] left = buffers[CHANNEL_LEFT].array();
                float[] right = buffers[CHANNEL_RIGHT].array();
                int bufferlen = buffers[CHANNEL_LEFT].getSize();
                float amp;
                amp = (float) (volume_left * volume_left);
                for (int i = 0; i < bufferlen; i++) left[i] *= amp;
                if (nrofchannels != 1) {
                    amp = (float) (volume_right * volume_right);
                    for (int i = 0; i < bufferlen; i++) right[i] *= amp;
                }
            }
        }
        if (buffers[CHANNEL_LEFT].isSilent() && buffers[CHANNEL_RIGHT].isSilent()) {
            int midimessages_size;
            synchronized (control_mutex) {
                midimessages_size = midimessages.size();
            }
            if (midimessages_size == 0) {
                pusher_silent_count++;
                if (pusher_silent_count > 5) {
                    pusher_silent_count = 0;
                    synchronized (control_mutex) {
                        pusher_silent = true;
                        if (synth.weakstream != null) synth.weakstream.setInputStream(null);
                    }
                }
            }
        } else pusher_silent_count = 0;
        if (synth.agc_on) agc.processAudio();
    }
