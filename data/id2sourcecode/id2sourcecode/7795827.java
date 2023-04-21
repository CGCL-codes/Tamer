            public void run() throws Exception {
                DashboardDeserializer dashboardDeserializer = new DashboardDeserializer();
                setDashboard(dashboardDeserializer.deserialize(getFile(), null));
                if (getGraphicalViewer() != null) {
                    getGraphicalViewer().setContents(dashboard);
                    updateRulers();
                }
            }
