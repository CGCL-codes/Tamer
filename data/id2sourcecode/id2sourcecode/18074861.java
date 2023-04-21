    protected int getChannel(ISoundOrigin origin, sfxinfo_t sfxinfo) {
        int cnum;
        channel_t c;
        for (cnum = 0; cnum < numChannels; cnum++) {
            if (channels[cnum].sfxinfo == null) break; else if (origin != null && channels[cnum].origin == origin) {
                StopChannel(cnum);
                break;
            }
        }
        if (cnum == numChannels) {
            for (cnum = 0; cnum < numChannels; cnum++) if (channels[cnum].sfxinfo.priority >= sfxinfo.priority) break;
            if (cnum == numChannels) {
                return -1;
            } else {
                StopChannel(cnum);
            }
        }
        c = channels[cnum];
        c.sfxinfo = sfxinfo;
        c.origin = origin;
        return cnum;
    }
