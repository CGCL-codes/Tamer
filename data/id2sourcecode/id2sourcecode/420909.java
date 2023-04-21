    void copyFromClipboard() {
        Component component = getFocusOwner();
        if (component instanceof TextComponent) cap.transferFromClipboard((TextComponent) component);
    }
