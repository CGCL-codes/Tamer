    public Module getModule(JDesktopPane desktop) {
        AlertReader reader = AlertManager.getReader();
        AlertWriter writer = AlertManager.getWriter();
        Module alertModule = null;
        try {
            if (symbols == null) {
                alertModule = new AlertModule(desktop, reader, writer);
            } else {
                alertModule = new AlertModule(desktop, symbols, reader, writer);
            }
        } catch (AlertException e) {
        } finally {
            return alertModule;
        }
    }
