    public void close(final boolean save) {
        enableSanityChecking(false);
        Display display = getSite().getShell().getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                if (getGraphicalViewer() != null) getSite().getPage().closeEditor(MyDiagramDocumentEditor.this, save);
            }
        });
    }
