    @Override
    public void sessionClosed(IoSession session) {
        DebuggeeJoint joint = (DebuggeeJoint) session.getAttribute("joint");
        if (joint != null) joint.sessionClosed(false);
        if (LOG.isDebugEnabled()) LOG.debug("Total " + session.getReadBytes() + " byte(s) readed, " + session.getWrittenBytes() + " byte(s) writed.");
    }
