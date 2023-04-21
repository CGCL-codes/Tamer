    @Override
    protected void initializeGraphicalViewer() {
        ISarosSession session = sessionManager.getSarosSession();
        if (session != null) {
            setEnabled(session.getLocalUser().hasWriteAccess());
        }
    }
