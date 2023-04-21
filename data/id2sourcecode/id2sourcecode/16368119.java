            public void run() throws Exception {
                setDashboard(DashboardSerializer.getInstance().deserialize(getFile(), null));
                if (getGraphicalViewer() != null) {
                    getGraphicalViewer().setContents(dashboard);
                    updateRulers();
                }
            }
