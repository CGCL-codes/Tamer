    protected void setInput(IEditorInput input) {
        super.setInput(input);
        try {
            if (input instanceof NullEditorInput && StringUtil.isNotNull(((NullEditorInput) input).getToolTipText())) {
                String path = ((NullEditorInput) input).getToolTipText();
                URL url = FileLocator.find(ShapesPlugin.getDefault().getBundle(), new Path(path), null);
                InputStream in = url.openStream();
                diagram = (ShapesDiagram) xstream.fromXML(in);
                in.close();
            } else {
                File file = ((IPathEditorInput) input).getPath().toFile();
                FileInputStream fis = new FileInputStream(file);
                diagram = (ShapesDiagram) xstream.fromXML(fis);
                fis.close();
                setPartName(file.getName());
            }
        } catch (Exception e) {
            handleLoadException(e);
        }
    }
