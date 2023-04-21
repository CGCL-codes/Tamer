    public void processApplicationPaneEvent(AWTEvent e) {
        WhiteBoardServerAction action = new WhiteBoardServerAction(AppContext.instance().getUser());
        GUIPane guiPane = (GUIPane) AppContext.instance().getObject("applicationPane");
        if (!(guiPane instanceof ApplicationPane)) {
            return;
        }
        ApplicationPane appPane = (ApplicationPane) guiPane;
        switch(e.getID()) {
            case MouseEvent.MOUSE_MOVED:
                {
                    MouseEvent event = (MouseEvent) e;
                    Point glassPanePos = ((SwingGUIPane) appPane).getPanel().getLocationOnScreen();
                    Point compPos = ((JComponent) e.getSource()).getLocationOnScreen();
                    pointerX = (int) (compPos.getX() - glassPanePos.getX() + event.getX());
                    pointerY = (int) (compPos.getY() - glassPanePos.getY() + event.getY());
                    action.sendMouseMove(pointerX, pointerY);
                    break;
                }
            case KeyEvent.KEY_RELEASED:
                {
                    KeyEvent event = (KeyEvent) e;
                    switch(event.getKeyCode()) {
                        case KeyEvent.VK_F1:
                            appPane.contextHelp();
                            break;
                        case KeyEvent.VK_F5:
                            action.sendPaint(pointerX, pointerY, WhiteBoardAction.PAINT_EXCLAMATION);
                            break;
                        case KeyEvent.VK_F6:
                            action.sendPaint(pointerX, pointerY, WhiteBoardAction.PAINT_INFO);
                            break;
                        case KeyEvent.VK_F7:
                            action.sendPaint(pointerX, pointerY, WhiteBoardAction.PAINT_OK);
                            break;
                        case KeyEvent.VK_F8:
                            action.sendPaint(pointerX, pointerY, WhiteBoardAction.PAINT_QUESTION);
                            break;
                        case KeyEvent.VK_F9:
                            action.sendPaint(pointerX, pointerY, WhiteBoardAction.PAINT_ERASE);
                            break;
                    }
                }
        }
        ClientTransceiver transceiver = new ClientTransceiver(AppContext.instance().getChannelNumber());
        transceiver.addReceiver(AppContext.instance().getChannelNumber());
        action.setTransceiver(transceiver);
        ActionTools.sendToServer(action);
    }
