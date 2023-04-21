    public StringBuilder getHelpText(String helpStr, Properties rHelpFile, MOB forMOB, boolean noFix) {
        helpStr = helpStr.toUpperCase().trim();
        if (helpStr.indexOf(" ") >= 0) helpStr = helpStr.replace(' ', '_');
        String thisTag = null;
        CharClass C = CMClass.findCharClass(helpStr.toUpperCase());
        if ((C != null) && (C.isGeneric())) thisTag = "<CHARCLASS>" + C.getStat("HELP");
        Race R = CMClass.findRace(helpStr.toUpperCase());
        if ((R != null) && (R.isGeneric())) thisTag = "<RACE>" + R.getStat("HELP");
        boolean found = false;
        if (thisTag == null) thisTag = rHelpFile.getProperty(helpStr);
        boolean areaTag = (thisTag == null) && helpStr.startsWith("AREAHELP_");
        if (thisTag == null) {
            thisTag = rHelpFile.getProperty("SPELL_" + helpStr);
            if (thisTag != null) helpStr = "SPELL_" + helpStr;
        }
        if (thisTag == null) {
            thisTag = rHelpFile.getProperty("PRAYER_" + helpStr);
            if (thisTag != null) helpStr = "PRAYER_" + helpStr;
        }
        if (thisTag == null) {
            thisTag = rHelpFile.getProperty("SONG_" + helpStr);
            if (thisTag != null) helpStr = "SONG_" + helpStr;
        }
        if (thisTag == null) {
            thisTag = rHelpFile.getProperty("DANCE_" + helpStr);
            if (thisTag != null) helpStr = "DANCE_" + helpStr;
        }
        if (thisTag == null) {
            thisTag = rHelpFile.getProperty("PLAY_" + helpStr);
            if (thisTag != null) helpStr = "PLAY_" + helpStr;
        }
        if (thisTag == null) {
            thisTag = rHelpFile.getProperty("CHANT_" + helpStr);
            if (thisTag != null) helpStr = "CHANT_" + helpStr;
        }
        if (thisTag == null) {
            thisTag = rHelpFile.getProperty("BEHAVIOR_" + helpStr);
            if (thisTag != null) helpStr = "BEHAVIOR_" + helpStr;
        }
        if (thisTag == null) {
            thisTag = rHelpFile.getProperty("POWER_" + helpStr);
            if (thisTag != null) helpStr = "POWER_" + helpStr;
        }
        if (thisTag == null) {
            thisTag = rHelpFile.getProperty("SKILL_" + helpStr);
            if (thisTag != null) helpStr = "SKILL_" + helpStr;
        }
        if (thisTag == null) {
            thisTag = rHelpFile.getProperty("PROP_" + helpStr);
            if (thisTag != null) helpStr = "PROP_" + helpStr;
        }
        found = ((thisTag != null) && (thisTag.length() > 0));
        if (!found) {
            String ahelpStr = helpStr.replaceAll("_", " ").trim();
            if (areaTag) ahelpStr = ahelpStr.substring(9);
            for (Enumeration e = CMLib.map().areas(); e.hasMoreElements(); ) {
                Area A = (Area) e.nextElement();
                if (A.name().equalsIgnoreCase(ahelpStr)) {
                    helpStr = A.name();
                    found = true;
                    areaTag = true;
                    break;
                }
            }
        }
        if ((!areaTag) && (!found)) {
            String ahelpStr = helpStr.replaceAll("_", " ").trim();
            if (!found) {
                String s = CMLib.socials().getSocialsHelp(forMOB, helpStr.toUpperCase(), true);
                if (s != null) {
                    thisTag = s;
                    helpStr = helpStr.toUpperCase();
                    found = true;
                }
            }
            if (!found) {
                Ability A = CMClass.findAbility(helpStr.toUpperCase(), -1, -1, true);
                if ((A != null) && (A.isGeneric())) {
                    thisTag = A.getStat("HELP");
                    found = true;
                }
            }
            if (!found) {
                String s = CMLib.expertises().getExpertiseHelp(helpStr.toUpperCase(), true);
                if (s != null) {
                    thisTag = s;
                    helpStr = helpStr.toUpperCase();
                    found = true;
                }
            }
            if (!found) {
                Deity D = CMLib.map().getDeity(helpStr);
                if (D != null) {
                    Command CMD = CMClass.getCommand("Deities");
                    Vector commands = CMParms.makeVector("DEITY", D);
                    try {
                        CMD.execute(forMOB, commands, Command.METAFLAG_FORCED);
                        helpStr = D.Name().toUpperCase();
                        if ((commands.size() == 1) && (commands.firstElement() instanceof String)) thisTag = (String) commands.firstElement();
                    } catch (Exception e) {
                    }
                }
            }
            if (!found) for (Enumeration e = rHelpFile.keys(); e.hasMoreElements(); ) {
                String key = ((String) e.nextElement()).toUpperCase();
                if (key.startsWith(helpStr)) {
                    thisTag = rHelpFile.getProperty(key);
                    helpStr = key;
                    found = true;
                    break;
                }
            }
            if (!found) {
                String currency = CMLib.english().matchAnyCurrencySet(ahelpStr);
                if (currency != null) {
                    double denom = CMLib.english().matchAnyDenomination(currency, ahelpStr);
                    if (denom > 0.0) {
                        Coins C2 = CMLib.beanCounter().makeCurrency(currency, denom, 1);
                        if ((C2 != null) && (C2.description().length() > 0)) return new StringBuilder(C2.name() + " is " + C2.description().toLowerCase());
                    }
                }
            }
            if (!found) for (Enumeration e = rHelpFile.keys(); e.hasMoreElements(); ) {
                String key = ((String) e.nextElement()).toUpperCase();
                if (CMLib.english().containsString(key, helpStr)) {
                    thisTag = rHelpFile.getProperty(key);
                    helpStr = key;
                    found = true;
                    break;
                }
            }
            if (!found) {
                String s = CMLib.socials().getSocialsHelp(forMOB, helpStr.toUpperCase(), false);
                if (s != null) {
                    thisTag = s;
                    helpStr = helpStr.toUpperCase();
                    found = true;
                }
            }
            if (!found) {
                Ability A = CMClass.findAbility(helpStr.toUpperCase(), -1, -1, false);
                if ((A != null) && (A.isGeneric())) {
                    thisTag = A.getStat("HELP");
                    found = true;
                }
            }
            if (!found) for (Enumeration e = CMLib.map().areas(); e.hasMoreElements(); ) {
                Area A = (Area) e.nextElement();
                if (CMLib.english().containsString(A.name(), ahelpStr)) {
                    helpStr = A.name();
                    break;
                }
            }
            if (!found) {
                String s = CMLib.expertises().getExpertiseHelp(helpStr.toUpperCase(), false);
                if (s != null) {
                    thisTag = s;
                    helpStr = helpStr.toUpperCase();
                    found = true;
                }
            }
        }
        while ((thisTag != null) && (thisTag.length() > 0) && (thisTag.length() < 31) && (!areaTag)) {
            String thisOtherTag = rHelpFile.getProperty(thisTag);
            if ((thisOtherTag != null) && (thisOtherTag.equals(thisTag))) thisTag = null; else if (thisOtherTag != null) {
                helpStr = thisTag;
                thisTag = thisOtherTag;
            } else break;
        }
        if ((thisTag == null) || (thisTag.length() == 0)) if (CMLib.map().getArea(helpStr.trim()) != null) return new StringBuilder(CMLib.map().getArea(helpStr.trim()).getAreaStats().toString());
        if ((thisTag == null) || (thisTag.length() == 0)) {
            String s = CMLib.channels().getChannelName(helpStr.trim());
            boolean no = false;
            if (((s == null) || (s.length() == 0)) && (helpStr.toLowerCase().startsWith("no"))) {
                s = CMLib.channels().getChannelName(helpStr.trim().substring(2));
                no = true;
            }
            if ((s != null) && (s.length() > 0)) {
                if (no) thisTag = rHelpFile.getProperty("NOCHANNEL"); else thisTag = rHelpFile.getProperty("CHANNEL");
                thisTag = CMStrings.replaceAll(thisTag, "[CHANNEL]", s.toUpperCase());
                thisTag = CMStrings.replaceAll(thisTag, "[channel]", s.toLowerCase());
                String extra = no ? "" : CMLib.channels().getExtraChannelDesc(s);
                thisTag = CMStrings.replaceAll(thisTag, "[EXTRA]", extra);
                return new StringBuilder(thisTag);
            }
        }
        if ((thisTag == null) || (thisTag.length() == 0)) return null;
        if (noFix) return new StringBuilder(thisTag);
        return new StringBuilder(fixHelp(helpStr, thisTag, forMOB));
    }
