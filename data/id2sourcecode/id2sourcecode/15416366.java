    public int getActionTypeForMouseClick(int x, int y, TextureGraphNode n) {
        if (n.getOutputConnectionPoint().inside(x, y)) {
            return 2;
        }
        for (ConnectionPoint cp : n.getAllConnectionPointsVector()) {
            if (cp.inside(x, y)) return -cp.channelIndex - 1;
        }
        if ((x >= helpX + n.getX()) && (x <= (helpX + n.getX() + helpW)) && (y >= helpY + n.getY()) && (y <= (helpY + n.getY() + helpH))) {
            JOptionPane.showMessageDialog(this, n.getChannel().getHelpText(), n.getChannel().getName() + " Help", JOptionPane.PLAIN_MESSAGE);
            return 3;
        }
        return 1;
    }
