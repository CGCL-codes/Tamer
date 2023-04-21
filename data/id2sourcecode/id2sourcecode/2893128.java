    public CCalendar(final Composite parent, final int style) {
        super(parent, style);
        setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
        setLayout(new CalendarLayout());
        initImages();
        this.calendarService = CalendarPlugin.getDefault().getService(ICalendarService.class);
        this.calendarChangeSupport = CalendarPlugin.getDefault().getService(ICalendarChangeSupport.class);
        this.calendarChangeSupport.addTimeSpanListener(this.listener);
        createContents();
        this.acViewer.addSelectionChangedListener(this);
        this.tcViewer.addSelectionChangedListener(this);
        this.body.setWeights(new int[] { 20, 80 });
        this.acFigure = (TasksCalendarFigure) ((ActivitiesCalendarPart) this.acViewer.getContents()).getFigure();
        this.tcFigure = (TasksCalendarFigure) ((TasksCalendarPart) this.tcViewer.getContents()).getFigure();
        ((FigureCanvas) this.tcViewer.getControl()).getViewport().getVerticalRangeModel().addPropertyChangeListener(this);
        this.acViewer.setKeyHandler(new GraphicalViewerKeyHandler(this.acViewer) {

            @Override
            public boolean keyPressed(final KeyEvent event) {
                if (event.keyCode == SWT.DEL) {
                    List eObjects = new ArrayList();
                    for (Iterator i = CCalendar.this.acViewer.getSelectedEditParts().iterator(); i.hasNext(); ) {
                        Object model = ((EditPart) i.next()).getModel();
                        if (model instanceof EObject) {
                            eObjects.add(model);
                        }
                    }
                    CalypsoEdit.delete(eObjects);
                }
                return super.keyPressed(event);
            }
        });
        this.tcViewer.setKeyHandler(new GraphicalViewerKeyHandler(this.tcViewer) {

            @Override
            public boolean keyPressed(final KeyEvent event) {
                if (event.keyCode == SWT.DEL) {
                    List eObjects = new ArrayList();
                    for (Iterator i = CCalendar.this.tcViewer.getSelectedEditParts().iterator(); i.hasNext(); ) {
                        Object model = ((EditPart) i.next()).getModel();
                        if (model instanceof EObject) {
                            eObjects.add(model);
                        }
                    }
                    CalypsoEdit.delete(eObjects);
                }
                return super.keyPressed(event);
            }
        });
        updateButtons();
    }
