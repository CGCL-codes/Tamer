    @Override
    public void createPartControl(Composite parent) {
        m_selectionProvider = new CompoundSelectionProvider();
        getSite().setSelectionProvider(m_selectionProvider);
        m_projectStore = Activator.getDefault().getRepository().getProjectStore();
        m_workItemStore = Activator.getDefault().getRepository().getWorkItemStore();
        m_pageBook = new PageBook(parent, SWT.NONE);
        m_tableViewer = new TableViewer(m_pageBook, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
        TableLayout layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(10, 50, true));
        layout.addColumnData(new ColumnWeightData(10, 50, true));
        layout.addColumnData(new ColumnWeightData(10, 50, true));
        layout.addColumnData(new ColumnWeightData(15, 50, true));
        layout.addColumnData(new ColumnWeightData(15, 50, true));
        layout.addColumnData(new ColumnWeightData(40, 100, true));
        m_tableViewer.getTable().setHeaderVisible(true);
        m_tableViewer.getTable().setLinesVisible(true);
        m_tableViewer.getTable().setLayout(layout);
        TableColumn beginColumn = new TableColumn(m_tableViewer.getTable(), SWT.LEFT);
        beginColumn.setText("Begin");
        beginColumn.setMoveable(true);
        TableColumn endColumn = new TableColumn(m_tableViewer.getTable(), SWT.LEFT);
        endColumn.setText("End");
        endColumn.setMoveable(true);
        TableColumn durationColumn = new TableColumn(m_tableViewer.getTable(), SWT.LEFT);
        durationColumn.setText("Duration");
        durationColumn.setMoveable(true);
        TableColumn projectColumn = new TableColumn(m_tableViewer.getTable(), SWT.LEFT);
        projectColumn.setText("Project");
        projectColumn.setMoveable(true);
        TableColumn todoColumn = new TableColumn(m_tableViewer.getTable(), SWT.LEFT);
        todoColumn.setText("Task");
        todoColumn.setMoveable(true);
        TableColumn commentColumn = new TableColumn(m_tableViewer.getTable(), SWT.LEFT);
        commentColumn.setText("Comment");
        commentColumn.setMoveable(true);
        m_tableViewer.setColumnProperties(new String[] { "begin", "end", "duration", "project", "task", "comment" });
        m_tableViewer.setContentProvider(new WorkItemContentProvider(m_workItemStore));
        m_tableViewer.setLabelProvider(new WorkItemTableLabelProvider(m_projectStore, false));
        m_selectedDay = Calendar.getInstance();
        m_tableViewer.setInput(m_selectedDay);
        m_tableViewer.setCellEditors(new CellEditor[] { new TimeComboCellEditor(m_tableViewer.getTable()), new TimeComboCellEditor(m_tableViewer.getTable()), null, null, new ComboViewerCellEditor(m_tableViewer.getTable(), new TaskContentProvider(m_projectStore, false), new TaskLabelProvider()), new TextCellEditor(m_tableViewer.getTable()) });
        m_tableViewer.setCellModifier(new WorkItemCellModifier());
        m_tableViewer.addDropSupport(DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT, new Transfer[] { TaskTransfer.getInstance() }, new DropTargetAdapter() {

            @Override
            public void drop(DropTargetEvent event) {
                if ((event.data == null) || !(event.data instanceof TaskTransfer.ProjectTask)) {
                    return;
                }
                doDropTask((TaskTransfer.ProjectTask) event.data);
            }
        });
        m_treeViewer = new TreeViewer(m_pageBook, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
        layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(30, 50, true));
        layout.addColumnData(new ColumnWeightData(10, 50, true));
        layout.addColumnData(new ColumnWeightData(10, 50, true));
        layout.addColumnData(new ColumnWeightData(10, 50, true));
        layout.addColumnData(new ColumnWeightData(40, 100, true));
        m_treeViewer.getTree().setLayout(layout);
        m_treeViewer.getTree().setHeaderVisible(true);
        m_treeViewer.getTree().setLinesVisible(true);
        TreeColumn projectTaskColumn = new TreeColumn(m_treeViewer.getTree(), SWT.LEFT);
        projectTaskColumn.setText("Project / Task");
        TreeColumn tBeginColumn = new TreeColumn(m_treeViewer.getTree(), SWT.LEFT);
        tBeginColumn.setText("Begin");
        TreeColumn tEndColumn = new TreeColumn(m_treeViewer.getTree(), SWT.LEFT);
        tEndColumn.setText("End");
        TreeColumn tDurationColumn = new TreeColumn(m_treeViewer.getTree(), SWT.LEFT);
        tDurationColumn.setText("Duration");
        TreeColumn tCommentColumn = new TreeColumn(m_treeViewer.getTree(), SWT.LEFT);
        tCommentColumn.setText("Comment");
        m_treeViewer.setColumnProperties(new String[] { "projectTask", "begin", "end", "duration", "comment" });
        m_treeViewer.setContentProvider(new WorkItemTreeContentProvider(m_projectStore, m_workItemStore));
        m_treeViewer.setLabelProvider(new WorkItemTreeLabelProvider(m_projectStore));
        MenuManager menuMgr = new MenuManager();
        menuMgr.add(new GroupMarker("newGroup"));
        menuMgr.add(new Separator());
        menuMgr.add(new GroupMarker("objectGroup"));
        menuMgr.add(new Separator());
        menuMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        Menu menu = menuMgr.createContextMenu(m_tableViewer.getControl());
        m_tableViewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, getSite().getPage().findView(TaskListView.ID).getSite().getSelectionProvider());
        getSite().registerContextMenu(menuMgr, m_tableViewer);
        m_tableViewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                try {
                    ICommandService commandService = (ICommandService) getSite().getWorkbenchWindow().getWorkbench().getService(ICommandService.class);
                    Command command = commandService.getCommand(ICommandIds.CMD_WORKITEM_EDIT);
                    command.executeWithChecks(new ExecutionEvent());
                } catch (Exception e) {
                    Activator.getDefault().log(e);
                }
            }
        });
        m_treeViewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                try {
                    ICommandService commandService = (ICommandService) getSite().getWorkbenchWindow().getWorkbench().getService(ICommandService.class);
                    Command command = commandService.getCommand(ICommandIds.CMD_WORKITEM_EDIT);
                    command.executeWithChecks(new ExecutionEvent());
                } catch (Exception e) {
                    Activator.getDefault().log(e);
                }
            }
        });
        m_dayGraphViewer = new ScrollingGraphicalViewer();
        m_dayGraphViewer.createControl(m_pageBook);
        m_dayGraphViewer.setEditPartFactory(new DayGraphEditPartFactory(Activator.getDefault().getRepository()));
        m_dayGraphViewer.setRootEditPart(new ScalableRootEditPart());
        m_dayGraphViewer.setSelectionManager(new ModelSelectionManager());
        m_dayGraphViewer.setEditDomain(new EditDomain());
        m_dayGraphViewer.addDropTargetListener(new AbstractTransferDropTargetListener(m_dayGraphViewer, TaskTransfer.getInstance()) {

            @Override
            protected Request createTargetRequest() {
                return new CreateRequest();
            }

            @Override
            protected void updateTargetRequest() {
                CreateRequest request = (CreateRequest) getTargetRequest();
                request.setLocation(getDropLocation());
                if ((getCurrentEvent().data != null) && (getCurrentEvent().data instanceof TaskTransfer.ProjectTask)) {
                    final TaskTransfer.ProjectTask projectTask = (TaskTransfer.ProjectTask) getCurrentEvent().data;
                    request.setFactory(new CreationFactory() {

                        public Object getNewObject() {
                            WorkItem workItem = new WorkItem();
                            workItem.setProjectId(projectTask.getProject().getId());
                            workItem.setTaskId(projectTask.getTask().getId());
                            workItem.setComment("");
                            return workItem;
                        }

                        public Object getObjectType() {
                            return null;
                        }
                    });
                }
            }
        });
        Activator.getDefault().getRepository().addRepositoryListener(RepositoryEventType.PROJECT, this);
        Activator.getDefault().getRepository().addRepositoryListener(RepositoryEventType.TASK, this);
        Activator.getDefault().getRepository().addRepositoryListener(RepositoryEventType.WORKITEM, this);
        m_pageBook.showPage(m_tableViewer.getTable());
        m_activeViewType = ViewType.FLAT;
        m_selectionProvider.setSelectionProvider(m_tableViewer);
        getSite().getPage().addSelectionListener(ProjectTreeView.ID, this);
        getSite().getPage().addSelectionListener(TaskListView.ID, this);
        getSite().getPage().addSelectionListener(CalendarView.ID, this);
    }
