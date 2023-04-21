            public void run() {
                int currentPage = ViewpointEditor.this.getActivePage();
                initializeGraphicalViewer();
                ViewpointEditor.this.removePage(0);
                ViewpointEditor.this.removePage(0);
                ViewpointEditor.this.removePage(0);
                createEditorPages();
                diagram.refreshChildren();
                updatePartName();
                ViewpointEditor.this.setActivePage(currentPage);
            }
