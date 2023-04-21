        public PasteCompoundCommand(String title, Hashtable<IDiagramModelObject, IDiagramModelObject> tempOriginalToNewMapping, GraphicalViewer viewer) {
            super(title);
            this.tempOriginalToNewMapping = tempOriginalToNewMapping;
            fViewer = viewer;
        }
