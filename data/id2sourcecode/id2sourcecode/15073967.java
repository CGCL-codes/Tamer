    private synchronized void updateState(Range intvl) {
        assert leasedBlocks.contains(intvl) : "trying to write an interval " + intvl + " that wasn't leased.\n" + dumpState();
        assert !(partialBlocks.contains(intvl) || savedCorruptBlocks.contains(intvl) || pendingBlocks.contains(intvl)) : "trying to write an interval " + intvl + " that was already written" + dumpState();
        leasedBlocks.delete(intvl);
        if (verifiedBlocks.containsAny(intvl)) {
            IntervalSet remaining = new IntervalSet();
            remaining.add(intvl);
            remaining.delete(verifiedBlocks);
            pendingBlocks.add(remaining);
        } else {
            pendingBlocks.add(intvl);
        }
    }
