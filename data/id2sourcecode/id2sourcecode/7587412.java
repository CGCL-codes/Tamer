    public boolean mayReadThisChannel(MOB sender, boolean areaReq, MOB M, int i, boolean offlineOK) {
        if ((sender == null) || (M == null) || (M.playerStats() == null)) return false;
        Room R = M.location();
        if (((!offlineOK)) && ((M.amDead()) || (R == null))) return false;
        if (getChannelFlags(i).contains(ChannelFlag.CLANONLY) || getChannelFlags(i).contains(ChannelFlag.CLANALLYONLY)) {
            if ((M.getClanID().length() == 0) || (M.getClanRole() == Clan.POS_APPLICANT)) return false;
            if ((!sender.getClanID().equalsIgnoreCase("ALL")) && (!M.getClanID().equalsIgnoreCase(sender.getClanID())) && ((!getChannelFlags(i).contains(ChannelFlag.CLANALLYONLY)) || (CMLib.clans().getClanRelations(M.getClanID(), sender.getClanID()) != Clan.REL_ALLY))) return false;
        }
        if ((!M.playerStats().getIgnored().contains(sender.Name())) && (CMLib.masking().maskCheck(getChannelMask(i), M, true)) && ((!areaReq) || (sender.location() == null) || (R.getArea() == sender.location().getArea())) && (!CMath.isSet(M.playerStats().getChannelMask(), i))) return true;
        return false;
    }
