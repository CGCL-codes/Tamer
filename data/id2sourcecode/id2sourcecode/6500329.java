                @Override
                public void doEdit() {
                    new DommainEditAction((GraphicalViewer) UIUtils.getActiveEditor().getAdapter(GraphicalViewer.class)).run();
                }
