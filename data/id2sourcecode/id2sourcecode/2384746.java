    protected boolean isAllowed(Object source) {
        if (!Desktop.isDesktopSupported()) {
            return false;
        }
        if (!Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            return false;
        }
        Grid grid = (Grid) source;
        Persistent reg = grid.getRegister();
        JXPathContext context = JXPathContext.newContext(reg);
        Object o = context.getValue(fieldMapping);
        return o != null;
    }
