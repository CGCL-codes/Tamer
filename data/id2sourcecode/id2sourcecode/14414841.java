    public GenerateAction(IGenerator generater, GraphicalViewer viewer, VisualDBEditor editor) {
        super(generater.getGeneratorName());
        this.generater = generater;
        this.editor = editor;
        this.viewer = viewer;
    }
