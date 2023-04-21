    public int getActionTypeForMouseClick(int x, int y) {
        if (node.getOutputConnectionPoint().inside(x, y)) {
            return 2;
        }
        Vector<ConnectionPoint> allCPs = node.getAllConnectionPointsVector();
        for (int i = 0; i < allCPs.size(); i++) {
            ConnectionPoint cp = allCPs.get(i);
            if (cp.inside(x, y)) return -cp.channelIndex - 1;
        }
        if ((x >= helpX) && (x <= (helpX + helpW)) && (y >= helpY) && (y <= (helpY + helpH))) {
            JOptionPane.showMessageDialog(this, node.getChannel().getHelpText(), node.getChannel().getName() + " Help", JOptionPane.PLAIN_MESSAGE);
            return 3;
        }
        return 1;
    }
