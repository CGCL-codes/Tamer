    @Override
    public void run() {
        GraphicalViewer[] viewers = new GraphicalViewer[] { this.calendar.getTasksViewer(), this.calendar.getActivitiesViewer() };
        if (isChecked()) {
            for (int i = 0; i < viewers.length; i++) {
                ((TasksCalendarFigure) ((GraphicalEditPart) viewers[i].getContents()).getFigure()).setGridVisible(true);
            }
        } else {
            for (int i = 0; i < viewers.length; i++) {
                ((TasksCalendarFigure) ((GraphicalEditPart) viewers[i].getContents()).getFigure()).setGridVisible(false);
            }
        }
    }
