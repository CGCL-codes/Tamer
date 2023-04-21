    public Object getAdapter(Class type) {
        if (type == IContentOutlinePage.class) {
            outlinePage = new OutlinePage(new TreeViewer());
            return outlinePage;
        }
        if (type == ZoomManager.class) return getGraphicalViewer().getProperty(ZoomManager.class.toString());
        return super.getAdapter(type);
    }
