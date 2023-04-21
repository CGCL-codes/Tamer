    public void blockOk() throws NotJoinedException, JGCSException {
        if (!isJoined()) throw new NotJoinedException();
        AppiaMembership am = (AppiaMembership) getMembership();
        if (am.blockEvent != null) {
            JGCSReleaseBlock releaseBlock = new JGCSReleaseBlock();
            releaseBlock.setBlockEvent(am.blockEvent);
            try {
                releaseBlock.asyncGo(am.blockEvent.getChannel(), Direction.DOWN);
            } catch (AppiaEventException e) {
                throw new JGCSException("Error releasing block appia event.", e);
            }
            logger.debug("Block released to the Appia channel");
            am.blockEvent = null;
        }
    }
