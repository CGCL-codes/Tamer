    public boolean mayReadThisChannel(MOB sender, boolean areaReq, Session ses, int i) {
        if (ses == null) return false;
        MOB M = ses.mob();
        if ((sender == null) || (M == null) || (M.amDead()) || (M.location() == null) || (M.playerStats() == null)) return false;
        String senderName = sender.Name();
        int x = senderName.indexOf("@");
        if (x > 0) senderName = senderName.substring(0, x);
        if (getChannelFlags(i).contains(ChannelFlag.CLANONLY) || getChannelFlags(i).contains(ChannelFlag.CLANALLYONLY)) {
            if ((M.getClanID().length() == 0) || (M.getClanRole() == Clan.POS_APPLICANT)) return false;
            if ((!sender.getClanID().equalsIgnoreCase("ALL")) && (!M.getClanID().equalsIgnoreCase(sender.getClanID())) && ((!getChannelFlags(i).contains(ChannelFlag.CLANALLYONLY)) || (CMLib.clans().getClanRelations(M.getClanID(), sender.getClanID()) != Clan.REL_ALLY))) return false;
        }
        Room R = M.location();
        if ((!ses.killFlag()) && (R != null) && (!M.playerStats().getIgnored().contains(senderName)) && (CMLib.masking().maskCheck(getChannelMask(i), M, true)) && ((!areaReq) || (sender.location() == null) || (R.getArea() == sender.location().getArea())) && (!CMath.isSet(M.playerStats().getChannelMask(), i))) return true;
        return false;
    }
