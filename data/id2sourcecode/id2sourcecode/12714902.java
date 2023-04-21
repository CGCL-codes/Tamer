        public void elementMoved(final Object originalElement, final Object movedElement) {
            if (originalElement != null && originalElement.equals(getEditorInput())) {
                final boolean doValidationAsync = Display.getCurrent() != null;
                Runnable r = new Runnable() {

                    public void run() {
                        enableSanityChecking(true);
                        if (getGraphicalViewer() == null) return;
                        if (!canHandleMove((IEditorInput) originalElement, (IEditorInput) movedElement)) {
                            close(true);
                            return;
                        }
                        if (movedElement == null || movedElement instanceof IEditorInput) {
                            final IDocumentProvider d = getDocumentProvider();
                            final Object previousContent;
                            IDocument changed = null;
                            IEditorInput oldInput = getEditorInput();
                            final boolean initialDirtyState = isDirty();
                            if (initialDirtyState || reuseDiagramOnMove()) {
                                changed = d.getDocument(oldInput);
                                if (changed != null) {
                                    if (changed instanceof IDiagramDocument) previousContent = ((IDiagramDocument) changed).detachDiagram(); else previousContent = changed.getContent();
                                } else previousContent = null;
                            } else previousContent = null;
                            try {
                                doSetInput((IEditorInput) movedElement, !(changed != null));
                            } catch (CoreException e) {
                                String title = EditorMessages.Editor_error_setinput_title;
                                String msg = EditorMessages.Editor_error_setinput_message;
                                Shell shell = getSite().getShell();
                                ErrorDialog.openError(shell, title, msg, e.getStatus());
                            }
                            if (changed != null && previousContent != null) {
                                Runnable r2 = new Runnable() {

                                    public void run() {
                                        validateState(getEditorInput());
                                        getDocumentProvider().getDocument(getEditorInput()).setContent(previousContent);
                                        if (reuseDiagramOnMove() && !initialDirtyState) {
                                            try {
                                                getDocumentProvider().resetDocument(getEditorInput());
                                            } catch (CoreException e) {
                                                String title = EditorMessages.Editor_error_setinput_title;
                                                String msg = EditorMessages.Editor_error_setinput_message;
                                                Shell shell = getSite().getShell();
                                                ErrorDialog.openError(shell, title, msg, e.getStatus());
                                            }
                                        }
                                    }
                                };
                                execute(r2, doValidationAsync);
                            }
                        }
                    }
                };
                execute(r, false);
            }
        }
