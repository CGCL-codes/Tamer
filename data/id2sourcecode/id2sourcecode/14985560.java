    private boolean getBooleanProperty(String property) {
        if (property == null) {
            return false;
        }
        Object value = getGraphicalViewer().getProperty(property);
        if (value == null) {
            return false;
        }
        return ((Boolean) value).booleanValue();
    }
