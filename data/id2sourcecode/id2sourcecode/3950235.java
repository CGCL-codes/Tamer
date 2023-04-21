    public void run() {
        if (MessageDialog.openQuestion(null, "Warning", "Auto Layout will erase your layout settings. Proceed ?")) {
            execute(createLayoutCommand());
            editor.getGraphicalViewer().setSelection(new StructuredSelection());
            editor.flushStack();
            editor.makeDirty();
        }
    }
