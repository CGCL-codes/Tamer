    @Override
    public void mouseReleased(MouseEvent e) {
        mousePosition.x = (int) (e.getX() * zoom);
        mousePosition.y = (int) (e.getY() * zoom);
        int wsX = (int) ((mousePosition.x) * zoom) - desktopX;
        int wsY = (int) ((mousePosition.y) * zoom) - desktopY;
        if (nodeDragging || connectionDragging) notifyEditChangeListener();
        if (connectionDragging) {
            TextureGraphNode targetPat = graph.getNodeAtPosition(wsX, wsY);
            if (targetPat != null) {
                int actionType = getActionTypeForMouseClick(wsX, wsY, targetPat);
                if (actionType < 0) {
                    int index = -actionType - 1;
                    if (e.isControlDown()) {
                        for (int i = 0; i < targetPat.getChannel().getNumInputChannels(); i++) {
                            TextureGraphNode.ConnectionPoint ip = targetPat.getInputConnectionPointByChannelIndex(i);
                            graph.addConnection(new TextureNodeConnection(connectionSource, ip));
                        }
                    } else {
                        TextureGraphNode.ConnectionPoint inputPoint = targetPat.getInputConnectionPointByChannelIndex(index);
                        graph.addConnection(new TextureNodeConnection(connectionSource, inputPoint));
                    }
                }
            }
        }
        nodeDragging = false;
        connectionDragging = false;
        desktopDragging = false;
        draggedWindow = null;
        repaint();
    }
