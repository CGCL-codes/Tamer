    public void run() {
        if (MessageDialog.openQuestion(null, "Warning", "Relayout message")) {
            createLayoutCommand().execute();
            editor.getGraphicalViewer().setSelection(new StructuredSelection());
            editor.flushStack();
            editor.makeDirty();
        }
    }
