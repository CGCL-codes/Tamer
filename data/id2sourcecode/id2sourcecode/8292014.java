    public void prepGlassPane(AbstractButton activeButton) {
        List<Rectangle> rectangles = new ArrayList<Rectangle>();
        if (glassPane != null) {
            glassPane.dispose();
        }
        activeComponents.clear();
        activeComponents.add(activeButton);
        if (activeButton == readChangesButton || activeButton == readAllButton || activeButton == writeChangesButton || activeButton == writeAllButton) {
            if (activeButton == readChangesButton) {
                for (int i = 0; i < paneList.size(); i++) {
                    activeComponents.add(((PaneProgPane) paneList.get(i)).readChangesButton);
                }
            } else if (activeButton == readAllButton) {
                for (int i = 0; i < paneList.size(); i++) {
                    activeComponents.add(((PaneProgPane) paneList.get(i)).readAllButton);
                }
            } else if (activeButton == writeChangesButton) {
                for (int i = 0; i < paneList.size(); i++) {
                    activeComponents.add(((PaneProgPane) paneList.get(i)).writeChangesButton);
                }
            } else if (activeButton == writeAllButton) {
                for (int i = 0; i < paneList.size(); i++) {
                    activeComponents.add(((PaneProgPane) paneList.get(i)).writeAllButton);
                }
            }
            for (int i = 0; i < tabPane.getTabCount(); i++) {
                rectangles.add(tabPane.getUI().getTabBounds(tabPane, i));
            }
        }
        glassPane = new BusyGlassPane(activeComponents, rectangles, this.getContentPane(), this);
        this.setGlassPane(glassPane);
    }
