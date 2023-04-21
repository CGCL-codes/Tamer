    @Override
    public void UpdateSoundParams(int handle, int vol, int sep, int pitch) {
        int chan = this.getChannelFromHandle(handle);
        int leftvol = vol - ((vol * sep * sep) >> 16);
        sep = sep - 257;
        int rightvol = vol - ((vol * sep * sep) >> 16);
        if (rightvol < 0 || rightvol > 127) DS.I.Error("rightvol out of bounds");
        if (leftvol < 0 || leftvol > 127) DS.I.Error("leftvol out of bounds");
        channelleftvol_lookup[chan] = vol_lookup[leftvol];
        channelrightvol_lookup[chan] = vol_lookup[rightvol];
        this.channelstep[chan] = steptable[pitch];
        channelsend[chan] = this.lengths[this.channelids[chan]];
    }
