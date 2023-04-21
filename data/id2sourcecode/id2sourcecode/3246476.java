    private void createContextMenu() {
        GraphicalViewer[] gviewers = new GraphicalViewer[] { this.calendar.getActivitiesViewer(), this.calendar.getTasksViewer() };
        for (int i = 0; i < gviewers.length; i++) {
            if (gviewers[i].getControl() != null) {
                MenuManager manager = new MenuManager();
                Menu menu = manager.createContextMenu(gviewers[i].getControl());
                gviewers[i].getControl().setMenu(menu);
                manager.add(this.createAction);
                manager.add(this.editAction);
                manager.add(this.moveAction);
                manager.add(new Separator());
                manager.add(new Separator());
                manager.add(this.removeAction);
                manager.add(new Separator());
                manager.add(this.gridAction);
            }
        }
    }
