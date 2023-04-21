    public boolean equals(Object o) {
        if (o instanceof PatchChange) {
            PatchChange tmpChg = (PatchChange) o;
            if (tmpChg.getChannel() == getChannel() && tmpChg.getOffset() == getOffset() && tmpChg.getPatchNumber() == getPatchNumber()) {
                return true;
            }
        }
        return false;
    }
