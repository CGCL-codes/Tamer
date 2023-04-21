            public void run() {
                if (getGraphicalViewer() != null) getSite().getPage().closeEditor(MyDiagramDocumentEditor.this, save);
            }
