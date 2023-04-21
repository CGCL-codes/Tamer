    @Override
    public void UpdateSoundParams(int handle, int vol, int sep, int pitch) {
        int chan = this.getChannelFromHandle(handle);
        int leftvol = vol - ((vol * sep * sep) >> 16);
        sep = sep - 257;
        int rightvol = vol - ((vol * sep * sep) >> 16);
        if (rightvol < 0 || rightvol > 127) DS.I.Error("rightvol out of bounds");
        if (leftvol < 0 || leftvol > 127) DS.I.Error("leftvol out of bounds");
        MixMessage m = new MixMessage();
        m.update = true;
        m.channel = chan;
        m.leftvol_lookup = vol_lookup[leftvol];
        m.rightvol_lookup = vol_lookup[rightvol];
        m.step = steptable[pitch];
        m.end = lengths[channelids[chan]];
        MIXSRV.submitMixMessage(m);
    }
