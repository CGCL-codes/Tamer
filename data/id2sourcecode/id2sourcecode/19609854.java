    public void fillAttributes(Set readSet, Set writeSet, MDObject obj) throws Exception {
        if (!check(obj)) return;
        MDWfState state = obj.getWfState();
        for (MDActorAttribute aa : attributePermissions) {
            aa.fillAttributes(readSet, writeSet, state);
        }
    }
