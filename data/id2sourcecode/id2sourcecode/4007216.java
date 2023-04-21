    public Object findCommand(MOB mob, Vector commands) {
        if ((mob == null) || (commands == null) || (mob.location() == null) || (commands.size() == 0)) return null;
        String firstWord = ((String) commands.elementAt(0)).toUpperCase();
        if ((firstWord.length() > 1) && (!Character.isLetterOrDigit(firstWord.charAt(0)))) {
            commands.insertElementAt(((String) commands.elementAt(0)).substring(1), 1);
            commands.setElementAt("" + firstWord.charAt(0), 0);
            firstWord = "" + firstWord.charAt(0);
        }
        Command C = CMClass.findCommandByTrigger(firstWord, true);
        if ((C != null) && (C.securityCheck(mob)) && (!CMSecurity.isDisabled("COMMAND_" + CMClass.classID(C).toUpperCase()))) return C;
        Ability A = getToEvoke(mob, (Vector) commands.clone());
        if ((A != null) && (!CMSecurity.isDisabled("ABILITY_" + A.ID().toUpperCase()))) return A;
        if (getAnEvokeWord(mob, firstWord) != null) return null;
        Social social = CMLib.socials().fetchSocial(commands, true, true);
        if (social != null) return social;
        for (int c = 0; c < CMLib.channels().getNumChannels(); c++) {
            if (CMLib.channels().getChannelName(c).equalsIgnoreCase(firstWord)) {
                C = CMClass.getCommand("Channel");
                if ((C != null) && (C.securityCheck(mob))) return C;
            } else if (("NO" + CMLib.channels().getChannelName(c)).equalsIgnoreCase(firstWord)) {
                C = CMClass.getCommand("NoChannel");
                if ((C != null) && (C.securityCheck(mob))) return C;
            }
        }
        for (Enumeration<JournalsLibrary.CommandJournal> e = CMLib.journals().commandJournals(); e.hasMoreElements(); ) {
            JournalsLibrary.CommandJournal CMJ = e.nextElement();
            if (CMJ.NAME().equalsIgnoreCase(firstWord)) {
                C = CMClass.getCommand("CommandJournal");
                if ((C != null) && (C.securityCheck(mob))) return C;
            }
        }
        for (int a = 0; a < mob.numAbilities(); a++) {
            A = mob.fetchAbility(a);
            HashSet tried = new HashSet();
            if (A.triggerStrings() != null) for (int t = 0; t < A.triggerStrings().length; t++) if ((A.triggerStrings()[t].toUpperCase().startsWith(firstWord)) && (!tried.contains(A.triggerStrings()[t]))) {
                Vector commands2 = (Vector) commands.clone();
                commands2.setElementAt(A.triggerStrings()[t], 0);
                Ability A2 = getToEvoke(mob, commands2);
                if ((A2 != null) && (!CMSecurity.isDisabled("ABILITY_" + A2.ID().toUpperCase()))) {
                    commands.setElementAt(A.triggerStrings()[t], 0);
                    return A;
                }
            }
        }
        C = CMClass.findCommandByTrigger(firstWord, false);
        if ((C != null) && (C.securityCheck(mob)) && (!CMSecurity.isDisabled("COMMAND_" + CMClass.classID(C).toUpperCase()))) return C;
        social = CMLib.socials().fetchSocial(commands, false, true);
        if (social != null) {
            commands.setElementAt(social.baseName(), 0);
            return social;
        }
        for (int c = 0; c < CMLib.channels().getNumChannels(); c++) {
            if (CMLib.channels().getChannelName(c).startsWith(firstWord)) {
                commands.setElementAt(CMLib.channels().getChannelName(c), 0);
                C = CMClass.getCommand("Channel");
                if ((C != null) && (C.securityCheck(mob))) return C;
            } else if (("NO" + CMLib.channels().getChannelName(c)).startsWith(firstWord)) {
                commands.setElementAt("NO" + CMLib.channels().getChannelName(c), 0);
                C = CMClass.getCommand("NoChannel");
                if ((C != null) && (C.securityCheck(mob))) return C;
            }
        }
        for (Enumeration<JournalsLibrary.CommandJournal> e = CMLib.journals().commandJournals(); e.hasMoreElements(); ) {
            JournalsLibrary.CommandJournal CMJ = e.nextElement();
            if (CMJ.NAME().startsWith(firstWord)) {
                C = CMClass.getCommand("CommandJournal");
                if ((C != null) && (C.securityCheck(mob))) return C;
            }
        }
        return null;
    }
