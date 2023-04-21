    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        if (SwingUtilities.isEventDispatchThread()) {
            if (frameImp != null) {
                if (frameImp instanceof JFrame) {
                    ((ViewJFrame) frameImp).setIconImage(null);
                } else if (frameImp instanceof JInternalFrame) {
                    ((ViewJInternalFrame) frameImp).setFrameIcon(null);
                } else {
                    throw new RuntimeException("Internal Error: Unsupported frame type for externalizing: " + frameImp.getClass());
                }
                bounds = this.getFrameImp().getBounds();
                iconified = ((JInternalFrame) this.getFrameImp()).isIcon();
            }
            out.defaultWriteObject();
        } else {
            System.out.println(this + ": cannot write because not in Event Dispatch Thread!");
        }
    }
