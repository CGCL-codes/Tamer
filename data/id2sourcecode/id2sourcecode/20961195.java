        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
            try {
                monitor.beginTask(PropertyHandler.getInstance().getProperty("_validation_msg_synchronize"), 3);
                final IDocumentProvider provider = ((ExhibitionDiagramEditor) editor).getDocumentProvider();
                final IEditorInput input = ((ExhibitionDiagramEditor) editor).getEditorInput();
                if (!provider.isDeleted(input)) {
                    if (!provider.isSynchronized(input)) try {
                        provider.setProgressMonitor(monitor);
                        provider.synchronize(input);
                    } catch (CoreException e) {
                        ExceptionHandlingService.INSTANCE.handleException(e);
                    }
                }
                monitor.worked(1);
                monitor.setTaskName(PropertyHandler.getInstance().getProperty("_validation_msg_model"));
                if (!provider.isDeleted(input)) {
                    if (!provider.isSynchronized(input)) try {
                        provider.setProgressMonitor(monitor);
                        provider.synchronize(input);
                    } catch (CoreException e) {
                        ExceptionHandlingService.INSTANCE.handleException(e);
                    }
                }
                monitor.worked(1);
                monitor.setTaskName(PropertyHandler.getInstance().getProperty("_validation_msg_model"));
                new IRunnableWithProgress() {

                    public void run(IProgressMonitor monitor) throws InterruptedException, InvocationTargetException {
                        runValidation(editor.getDiagramEditPart(), editor.getDiagram());
                    }
                }.run(monitor);
                monitor.worked(1);
                monitor.setTaskName(PropertyHandler.getInstance().getProperty("_validation_msg_results"));
                ValidationResultProvider.INSTANCE.setResults(((IDiagramWorkbenchPart) editor).getDiagramGraphicalViewer(), ((IDiagramWorkbenchPart) editor).getDiagram());
                monitor.worked(1);
            } finally {
                monitor.done();
            }
        }
