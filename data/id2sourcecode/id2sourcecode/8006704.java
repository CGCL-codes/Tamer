    protected void build() {
        if (isPopupMenuVisible() || valid_) return;
        FuLog.debug("BMT: build menu internal frames");
        valid_ = true;
        removeAll();
        if (desktop_ == null) {
            number_ = 0;
            return;
        }
        JInternalFrame[] frames = desktop_.getAllFrames();
        number_ = frames.length;
        for (int i = 0; i < frames.length - 1; i++) {
            String t0 = frames[i].getTitle();
            String t1 = frames[i + 1].getTitle();
            if (t0 == null) System.err.println("No title for " + frames[i].getName());
            if ((t0 != null) && (t1 != null) && (t1.compareTo(t0) < 0)) {
                JInternalFrame tmp = frames[i];
                frames[i] = frames[i + 1];
                frames[i + 1] = tmp;
                i -= 2;
                if (i < 0) i = -1;
            }
        }
        for (int i = 0; i < frames.length; i++) {
            JInternalFrame f = frames[i];
            String n = f.getName();
            if ((n != null) && (f.getClientProperty("JInternalFrame.isPalette") != Boolean.TRUE)) {
                if (n.startsWith("if")) n = n.substring(2);
                BuMenuItem mi = addMenuItem(f.getTitle(), "FILLE_ACTIVER(" + n + ")", true, BuInternalFrame.getShortcut(f));
                Icon icon = f.getFrameIcon();
                if (icon instanceof BuIcon) mi.setIcon(BuResource.BU.reduceMenuIcon((BuIcon) icon)); else mi.setIcon(icon);
            }
        }
        computeMnemonics();
    }
