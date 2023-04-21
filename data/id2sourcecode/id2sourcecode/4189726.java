    public boolean execute(MOB mob, Vector commands, int metaFlags) throws java.io.IOException {
        if (commands.size() < 2) {
            mob.tell("Consider whom?");
            return false;
        }
        commands.removeElementAt(0);
        String targetName = CMParms.combine(commands, 0);
        MOB target = mob.location().fetchInhabitant(targetName);
        if ((target == null) || (!CMLib.flags().canBeSeenBy(target, mob))) {
            mob.tell("I don't see '" + targetName + "' here.");
            return false;
        }
        int relDiff = relativeLevelDiff(target, mob.getGroupMembers(new HashSet()));
        int lvlDiff = (target.envStats().level() - mob.envStats().level());
        int realDiff = (relDiff + lvlDiff) / 2;
        int theDiff = 2;
        if (mob.envStats().level() > 20) theDiff = 3;
        if (mob.envStats().level() > 40) theDiff = 4;
        if (mob.envStats().level() > 60) theDiff = 5;
        if (mob.envStats().level() > 80) theDiff = 6;
        int levelDiff = Math.abs(realDiff);
        if (levelDiff < theDiff) {
            mob.tell("The perfect match!");
            return false;
        } else if (realDiff < 0) {
            if (realDiff > -(2 * theDiff)) {
                mob.tell(target.charStats().HeShe() + " might give you a fight.");
                return false;
            } else if (realDiff > -(3 * theDiff)) {
                mob.tell(target.charStats().HeShe() + " is hardly worth your while.");
                return false;
            } else if (realDiff > -(4 * theDiff)) {
                mob.tell(target.charStats().HeShe() + " is a pushover.");
                return false;
            } else {
                mob.tell(target.charStats().HeShe() + " is not worth the effort.");
                return false;
            }
        } else if (realDiff < (2 * theDiff)) {
            mob.tell(target.charStats().HeShe() + " looks a little tough.");
            return false;
        } else if (realDiff < (3 * theDiff)) {
            mob.tell(target.charStats().HeShe() + " is a serious threat.");
            return false;
        } else if (realDiff < (4 * theDiff)) {
            mob.tell(target.charStats().HeShe() + " will clean your clock.");
            return false;
        } else {
            mob.tell(target.charStats().HeShe() + " WILL KILL YOU DEAD!");
            return false;
        }
    }
