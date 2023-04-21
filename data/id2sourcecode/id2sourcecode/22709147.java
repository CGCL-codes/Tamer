    public boolean okMessage(Environmental myHost, CMMsg msg) {
        if (msg.amITarget(this)) switch(msg.targetMinor()) {
            case CMMsg.TYP_WRITE:
                msg.source().tell("You are not allowed to write on " + name() + ". Try reading it.");
                return false;
        }
        return super.okMessage(myHost, msg);
    }
