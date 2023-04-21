    protected void processAudioLogic(SoftAudioBuffer[] buffer) {
        if (!audiostarted) return;
        int bufferlen = buffer[0].getSize();
        try {
            osc_buff[0] = buffer[SoftMainMixer.CHANNEL_LEFT_DRY].array();
            if (nrofchannels != 1) osc_buff[1] = buffer[SoftMainMixer.CHANNEL_RIGHT_DRY].array();
            int ret = osc_stream.read(osc_buff, 0, bufferlen);
            if (ret == -1) {
                stopping = true;
                return;
            }
            if (ret != bufferlen) {
                Arrays.fill(osc_buff[0], ret, bufferlen, 0f);
                if (nrofchannels != 1) Arrays.fill(osc_buff[1], ret, bufferlen, 0f);
            }
        } catch (IOException e) {
        }
        SoftAudioBuffer left = buffer[SoftMainMixer.CHANNEL_LEFT];
        SoftAudioBuffer right = buffer[SoftMainMixer.CHANNEL_RIGHT];
        SoftAudioBuffer mono = buffer[SoftMainMixer.CHANNEL_MONO];
        SoftAudioBuffer eff1 = buffer[SoftMainMixer.CHANNEL_EFFECT1];
        SoftAudioBuffer eff2 = buffer[SoftMainMixer.CHANNEL_EFFECT2];
        SoftAudioBuffer dleft = buffer[SoftMainMixer.CHANNEL_DELAY_LEFT];
        SoftAudioBuffer dright = buffer[SoftMainMixer.CHANNEL_DELAY_RIGHT];
        SoftAudioBuffer dmono = buffer[SoftMainMixer.CHANNEL_DELAY_MONO];
        SoftAudioBuffer deff1 = buffer[SoftMainMixer.CHANNEL_DELAY_EFFECT1];
        SoftAudioBuffer deff2 = buffer[SoftMainMixer.CHANNEL_DELAY_EFFECT2];
        SoftAudioBuffer leftdry = buffer[SoftMainMixer.CHANNEL_LEFT_DRY];
        SoftAudioBuffer rightdry = buffer[SoftMainMixer.CHANNEL_RIGHT_DRY];
        if (osc_stream_nrofchannels == 1) rightdry = null;
        if (!Double.isInfinite(co_filter_freq[0])) {
            filter_left.processAudio(leftdry);
            if (rightdry != null) filter_right.processAudio(rightdry);
        }
        if (nrofchannels == 1) {
            out_mixer_left = (out_mixer_left + out_mixer_right) / 2;
            mixAudioStream(leftdry, left, dleft, last_out_mixer_left, out_mixer_left);
            if (rightdry != null) mixAudioStream(rightdry, left, dleft, last_out_mixer_left, out_mixer_left);
        } else {
            if (rightdry == null && last_out_mixer_left == last_out_mixer_right && out_mixer_left == out_mixer_right) {
                mixAudioStream(leftdry, mono, dmono, last_out_mixer_left, out_mixer_left);
            } else {
                mixAudioStream(leftdry, left, dleft, last_out_mixer_left, out_mixer_left);
                if (rightdry != null) mixAudioStream(rightdry, right, dright, last_out_mixer_right, out_mixer_right); else mixAudioStream(leftdry, right, dright, last_out_mixer_right, out_mixer_right);
            }
        }
        if (rightdry == null) {
            mixAudioStream(leftdry, eff1, deff1, last_out_mixer_effect1, out_mixer_effect1);
            mixAudioStream(leftdry, eff2, deff2, last_out_mixer_effect2, out_mixer_effect2);
        } else {
            mixAudioStream(leftdry, eff1, deff1, last_out_mixer_effect1 * 0.5f, out_mixer_effect1 * 0.5f);
            mixAudioStream(leftdry, eff2, deff2, last_out_mixer_effect2 * 0.5f, out_mixer_effect2 * 0.5f);
            mixAudioStream(rightdry, eff1, deff1, last_out_mixer_effect1 * 0.5f, out_mixer_effect1 * 0.5f);
            mixAudioStream(rightdry, eff2, deff2, last_out_mixer_effect2 * 0.5f, out_mixer_effect2 * 0.5f);
        }
        last_out_mixer_left = out_mixer_left;
        last_out_mixer_right = out_mixer_right;
        last_out_mixer_effect1 = out_mixer_effect1;
        last_out_mixer_effect2 = out_mixer_effect2;
        if (out_mixer_end) {
            stopping = true;
        }
    }
