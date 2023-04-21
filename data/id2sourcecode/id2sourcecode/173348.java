    void recheckBlockIcon(Block b) {
        BufferedImage blockArea = r.createScreenCapture(new Rectangle(b.x, b.y, cover.getWidth(), cover.getHeight()));
        Icon oldIcon = b.getIcon();
        State oldState = b.getState();
        Icon icon = whichIcon(blockArea, 0, 0, oldIcon);
        if (icon == null) {
            throw new IllegalStateException(Messages.getString("MineSweepRobot2.unknown.icon"));
        }
        if (icon == oldIcon) {
            return;
        }
        b.setIcon(icon);
        State newState = getInitState(icon);
        b.setState(newState);
        if (newState == State.S_UNSOLVED || newState == State.S_SOLVED) {
            if (oldState == State.S_UNKNOWN) {
                removeBlockFromAllGroups(b, false);
            }
        }
        if (b.getState() == State.S_UNSOLVED) {
            addToBeCheckedBlocks(b);
            return;
        }
        if (icon == Icon.M_N0) {
            for (int i = 0; i < 8; i++) {
                Block t = getSurroundingBlock(b, i);
                if (t != null && t.getState() == State.S_UNKNOWN) {
                    recheckBlockIcon(t);
                }
            }
        }
    }
