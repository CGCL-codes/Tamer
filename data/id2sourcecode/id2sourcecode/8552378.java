    protected void processControlLogic() {
        if (stopping) {
            active = false;
            stopping = false;
            audiostarted = false;
            if (osc_stream != null) try {
                osc_stream.close();
            } catch (IOException e) {
            }
            if (stealer_channel != null) {
                stealer_channel.initVoice(this, stealer_performer, stealer_voiceID, stealer_noteNumber, stealer_velocity, 0, stealer_extendedConnectionBlocks, stealer_channelmixer, stealer_releaseTriggered);
                stealer_releaseTriggered = false;
                stealer_channel = null;
                stealer_performer = null;
                stealer_voiceID = -1;
                stealer_noteNumber = 0;
                stealer_velocity = 0;
                stealer_extendedConnectionBlocks = null;
                stealer_channelmixer = null;
            }
        }
        if (started) {
            audiostarted = true;
            ModelOscillator osc = performer.oscillators[0];
            osc_stream_off_transmitted = false;
            if (osc instanceof ModelWavetable) {
                try {
                    resampler.open((ModelWavetable) osc, synthesizer.getFormat().getSampleRate());
                    osc_stream = resampler;
                } catch (IOException e) {
                }
            } else {
                osc_stream = osc.open(synthesizer.getFormat().getSampleRate());
            }
            osc_attenuation = osc.getAttenuation();
            osc_stream_nrofchannels = osc.getChannels();
            if (osc_buff == null || osc_buff.length < osc_stream_nrofchannels) osc_buff = new float[osc_stream_nrofchannels][];
            if (osc_stream != null) osc_stream.noteOn(softchannel, this, noteOn_noteNumber, noteOn_velocity);
        }
        if (audiostarted) {
            if (portamento) {
                double note_delta = tunedKey - (co_noteon_keynumber[0] * 128);
                double note_delta_a = Math.abs(note_delta);
                if (note_delta_a < 0.0000000001) {
                    co_noteon_keynumber[0] = tunedKey * (1.0 / 128.0);
                    portamento = false;
                } else {
                    if (note_delta_a > softchannel.portamento_time) note_delta = Math.signum(note_delta) * softchannel.portamento_time;
                    co_noteon_keynumber[0] += note_delta * (1.0 / 128.0);
                }
                int[] c = performer.midi_connections[4];
                if (c == null) return;
                for (int i = 0; i < c.length; i++) processConnection(c[i]);
            }
            eg.processControlLogic();
            lfo.processControlLogic();
            for (int i = 0; i < performer.ctrl_connections.length; i++) processConnection(performer.ctrl_connections[i]);
            osc_stream.setPitch((float) co_osc_pitch[0]);
            int filter_type = (int) co_filter_type[0];
            double filter_freq;
            if (co_filter_freq[0] == 13500.0) filter_freq = 19912.126958213175; else filter_freq = 440.0 * Math.exp(((co_filter_freq[0]) - 6900.0) * (Math.log(2.0) / 1200.0));
            double q = co_filter_q[0] / 10.0;
            filter_left.setFilterType(filter_type);
            filter_left.setFrequency(filter_freq);
            filter_left.setResonance(q);
            filter_right.setFilterType(filter_type);
            filter_right.setFrequency(filter_freq);
            filter_right.setResonance(q);
            float gain = (float) Math.exp((-osc_attenuation + co_mixer_gain[0]) * (Math.log(10) / 200.0));
            if (co_mixer_gain[0] <= -960) gain = 0;
            if (soundoff) {
                stopping = true;
                gain = 0;
            }
            volume = (int) (Math.sqrt(gain) * 128);
            double pan = co_mixer_pan[0] * (1.0 / 1000.0);
            if (pan < 0) pan = 0; else if (pan > 1) pan = 1;
            if (pan == 0.5) {
                out_mixer_left = gain * 0.7071067811865476f;
                out_mixer_right = out_mixer_left;
            } else {
                out_mixer_left = gain * (float) Math.cos(pan * Math.PI * 0.5);
                out_mixer_right = gain * (float) Math.sin(pan * Math.PI * 0.5);
            }
            double balance = co_mixer_balance[0] * (1.0 / 1000.0);
            if (balance != 0.5) {
                if (balance > 0.5) out_mixer_left *= (1 - balance) * 2; else out_mixer_right *= balance * 2;
            }
            if (synthesizer.reverb_on) {
                out_mixer_effect1 = (float) (co_mixer_reverb[0] * (1.0 / 1000.0));
                out_mixer_effect1 *= gain;
            } else out_mixer_effect1 = 0;
            if (synthesizer.chorus_on) {
                out_mixer_effect2 = (float) (co_mixer_chorus[0] * (1.0 / 1000.0));
                out_mixer_effect2 *= gain;
            } else out_mixer_effect2 = 0;
            out_mixer_end = co_mixer_active[0] < 0.5;
            if (!on) if (!osc_stream_off_transmitted) {
                osc_stream_off_transmitted = true;
                if (osc_stream != null) osc_stream.noteOff(noteOff_velocity);
            }
        }
        if (started) {
            last_out_mixer_left = out_mixer_left;
            last_out_mixer_right = out_mixer_right;
            last_out_mixer_effect1 = out_mixer_effect1;
            last_out_mixer_effect2 = out_mixer_effect2;
            started = false;
        }
    }
