    public boolean equalContent(Object o) {
        if (o instanceof Performative) {
            Performative other = (Performative) o;
            if (this.getTime().equals(other.getTime()) && this.getChannel().equals(other.getChannel()) && this.getSender().equals(other.getSender()) && this.getAction().equals(other.getAction()) && this.getContent().equals(other.getContent()) && this.getScope().equals(other.getScope()) && this.getDirection().toString().equals(other.getDirection().toString()) && this.getEntityIDs().size() == other.getEntityIDs().size()) {
                boolean checkOveral = true;
                for (EntityID eid : this.getEntityIDs()) {
                    boolean check = false;
                    for (EntityID eidOther : other.getEntityIDs()) {
                        if (eid.getEmid().getValue().equals(eidOther.getEmid().getValue()) && eid.getEsid().getValue().equals(eidOther.getEsid().getValue())) {
                            check = true;
                            break;
                        }
                    }
                    if (!check) {
                        checkOveral = false;
                        break;
                    }
                }
                for (String receiver : this.getReceivers()) {
                    boolean check = false;
                    for (String receiverOther : other.getReceivers()) {
                        if (receiver.equals(receiverOther)) {
                            check = true;
                            break;
                        }
                    }
                    if (!check) {
                        checkOveral = false;
                        break;
                    }
                }
                if (checkOveral) {
                    return true;
                }
            }
        }
        return false;
    }
