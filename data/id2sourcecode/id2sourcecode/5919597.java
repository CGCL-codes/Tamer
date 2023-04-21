    @Override
    protected void createGraphicalViewer(Composite parent) {
        final GraphicalViewer viewer = new ScrollingGraphicalViewer() {

            protected boolean isNotifying = false;

            {
                WhiteboardManager.getInstance().getSXEMessageHandler().addNotificationListener(new NotificationListener() {

                    @Override
                    public void beforeNotification() {
                        isNotifying = true;
                    }

                    @Override
                    public void afterNotificaion() {
                        isNotifying = false;
                        fireSelectionChanged();
                        updateActions();
                    }
                });
            }

            @Override
            protected void fireSelectionChanged() {
                if (isNotifying) return;
                super.fireSelectionChanged();
            }
        };
        viewer.createControl(parent);
        setGraphicalViewer(viewer);
        configureGraphicalViewer();
        hookGraphicalViewer();
        initializeGraphicalViewer();
    }
