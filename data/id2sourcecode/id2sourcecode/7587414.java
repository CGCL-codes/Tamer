    public boolean mayReadThisChannel(MOB M, int i, boolean zapCheckOnly) {
        if (M == null) return false;
        if (i >= getNumChannels()) return false;
        if ((getChannelFlags(i).contains(ChannelFlag.CLANONLY) || getChannelFlags(i).contains(ChannelFlag.CLANALLYONLY)) && ((M.getClanID().length() == 0) || (M.getClanRole() == Clan.POS_APPLICANT))) return false;
        if (((zapCheckOnly) || ((!M.amDead()) && (M.location() != null))) && (CMLib.masking().maskCheck(getChannelMask(i), M, true)) && (!CMath.isSet(M.playerStats().getChannelMask(), i))) return true;
        return false;
    }
