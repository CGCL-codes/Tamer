            public void zoomChanged(double d) {
                Composite parent = scrollingGraphicalViewer.getControl().getParent();
                parent.layout();
            }
