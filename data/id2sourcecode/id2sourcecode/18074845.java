    public void StartSoundAtVolume(ISoundOrigin origin_p, int sfx_id, int volume) {
        boolean rc;
        int sep = 0;
        int pitch;
        int priority;
        sfxinfo_t sfx;
        int cnum;
        ISoundOrigin origin = (ISoundOrigin) origin_p;
        if (sfx_id < 1 || sfx_id > NUMSFX) {
            Exception e = new Exception();
            e.printStackTrace();
            DS.I.Error("Bad sfx #: %d", sfx_id);
        }
        sfx = S_sfx[sfx_id];
        if (sfx.link != null) {
            pitch = sfx.pitch;
            priority = sfx.priority;
            volume += sfx.volume;
            if (volume < 1) return;
            if (volume > snd_SfxVolume) volume = snd_SfxVolume;
        } else {
            pitch = NORM_PITCH;
            priority = NORM_PRIORITY;
        }
        if ((origin != null) && origin != DS.players[DS.consoleplayer].mo) {
            vps.volume = volume;
            vps.pitch = pitch;
            vps.sep = sep;
            rc = AdjustSoundParams(DS.players[DS.consoleplayer].mo, origin, vps);
            volume = vps.volume;
            pitch = vps.pitch;
            sep = vps.sep;
            if (origin.getX() == DS.players[DS.consoleplayer].mo.x && origin.getY() == DS.players[DS.consoleplayer].mo.y) {
                sep = NORM_SEP;
            }
            if (!rc) {
                return;
            }
        } else {
            sep = NORM_SEP;
        }
        if (sfx_id >= sfxenum_t.sfx_sawup.ordinal() && sfx_id <= sfxenum_t.sfx_sawhit.ordinal()) {
            pitch += 8 - (DS.RND.M_Random() & 15);
            if (pitch < 0) pitch = 0; else if (pitch > 255) pitch = 255;
        } else if (sfx_id != sfxenum_t.sfx_itemup.ordinal() && sfx_id != sfxenum_t.sfx_tink.ordinal()) {
            pitch += 16 - (DS.RND.M_Random() & 31);
            if (pitch < 0) pitch = 0; else if (pitch > 255) pitch = 255;
        }
        StopSound(origin);
        cnum = getChannel(origin, sfx);
        if (cnum < 0) return;
        if (sfx.lumpnum < 0) sfx.lumpnum = ISND.GetSfxLumpNum(sfx);
        if (sfx.usefulness++ < 0) sfx.usefulness = 1;
        channels[cnum].handle = ISND.StartSound(sfx_id, volume, sep, pitch, priority);
        if (D) System.err.printf("Handle %d for channel %d for sound %s vol %d sep %d\n", channels[cnum].handle, cnum, sfx.name, volume, sep);
    }
