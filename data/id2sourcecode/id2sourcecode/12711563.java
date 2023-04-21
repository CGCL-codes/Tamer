    public void DBUpdateJustMOB(MOB mob) {
        if (mob.Name().length() == 0) {
            DBCreateCharacter(mob);
            return;
        }
        PlayerStats pstats = mob.playerStats();
        if (pstats == null) return;
        String strStartRoomID = (mob.getStartRoom() != null) ? CMLib.map().getExtendedRoomID(mob.getStartRoom()) : "";
        String strOtherRoomID = (mob.location() != null) ? CMLib.map().getExtendedRoomID(mob.location()) : "";
        if ((mob.location() != null) && (mob.location().getArea() != null) && (CMath.bset(mob.location().getArea().flags(), Area.FLAG_INSTANCE_PARENT) || CMath.bset(mob.location().getArea().flags(), Area.FLAG_INSTANCE_CHILD))) strOtherRoomID = strStartRoomID;
        String pfxml = getPlayerStatsXML(mob);
        StringBuffer cleanXML = new StringBuffer();
        cleanXML.append(CMLib.coffeeMaker().getFactionXML(mob));
        DB.update("UPDATE CMCHAR SET  CMPASS='" + pstats.password() + "'" + ", CMCLAS='" + mob.baseCharStats().getMyClassesStr() + "'" + ", CMSTRE=" + mob.baseCharStats().getStat(CharStats.STAT_STRENGTH) + ", CMRACE='" + mob.baseCharStats().getMyRace().ID() + "'" + ", CMDEXT=" + mob.baseCharStats().getStat(CharStats.STAT_DEXTERITY) + ", CMCONS=" + mob.baseCharStats().getStat(CharStats.STAT_CONSTITUTION) + ", CMGEND='" + ((char) mob.baseCharStats().getStat(CharStats.STAT_GENDER)) + "'" + ", CMWISD=" + mob.baseCharStats().getStat(CharStats.STAT_WISDOM) + ", CMINTE=" + mob.baseCharStats().getStat(CharStats.STAT_INTELLIGENCE) + ", CMCHAR=" + mob.baseCharStats().getStat(CharStats.STAT_CHARISMA) + ", CMHITP=" + mob.baseState().getHitPoints() + ", CMLEVL='" + mob.baseCharStats().getMyLevelsStr() + "'" + ", CMMANA=" + mob.baseState().getMana() + ", CMMOVE=" + mob.baseState().getMovement() + ", CMALIG=-1" + ", CMEXPE=" + mob.getExperience() + ", CMEXLV=" + mob.getExpNextLevel() + ", CMWORS='" + mob.getWorshipCharID() + "'" + ", CMPRAC=" + mob.getPractices() + ", CMTRAI=" + mob.getTrains() + ", CMAGEH=" + mob.getAgeHours() + ", CMGOLD=" + mob.getMoney() + ", CMWIMP=" + mob.getWimpHitPoint() + ", CMQUES=" + mob.getQuestPoint() + ", CMROID='" + strStartRoomID + "||" + strOtherRoomID + "'" + ", CMDATE='" + pstats.lastDateTime() + "'" + ", CMCHAN=" + pstats.getChannelMask() + ", CMATTA=" + mob.baseEnvStats().attackAdjustment() + ", CMAMOR=" + mob.baseEnvStats().armor() + ", CMDAMG=" + mob.baseEnvStats().damage() + ", CMBTMP=" + mob.getBitmap() + ", CMLEIG='" + mob.getLiegeID() + "'" + ", CMHEIT=" + mob.baseEnvStats().height() + ", CMWEIT=" + mob.baseEnvStats().weight() + ", CMPRPT='" + pstats.getPrompt() + "'" + ", CMCOLR='" + pstats.getColorStr() + "'" + ", CMCLAN='" + mob.getClanID() + "'" + ", CMLSIP='" + pstats.lastIP() + "'" + ", CMCLRO=" + mob.getClanRole() + ", CMEMAL='" + pstats.getEmail() + "'" + ", CMPFIL='" + pfxml.toString() + "'" + ", CMSAVE='" + mob.baseCharStats().getNonBaseStatsAsString() + "'" + ", CMMXML='" + cleanXML.toString() + "'" + "  WHERE CMUSERID='" + mob.Name() + "'");
        DB.update("UPDATE CMCHAR SET CMDESC='" + mob.description() + "' WHERE CMUSERID='" + mob.Name() + "'");
    }
