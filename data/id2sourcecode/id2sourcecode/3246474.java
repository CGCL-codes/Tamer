    private void createActions() {
        this.zoomInAction = new ZoomInAction();
        this.zoomOutAction = new ZoomOutAction();
        this.gridAction = new ShowGridAction();
        this.createAction = new CreateAction(new GraphicalViewer[] { this.calendar.getTasksViewer(), this.calendar.getActivitiesViewer() });
        this.editAction = new EditModelAction(this.calendar);
        this.removeAction = new RemoveAction(this.calendar);
        this.moveAction = new MoveToTodayAction(this.calendar);
    }
