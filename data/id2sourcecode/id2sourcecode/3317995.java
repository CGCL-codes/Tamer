    public Term enter(String sAgent, APLNum xt, APLNum yt, APLIdent colort) throws ExternalActionFailedException {
        int x = xt.toInt();
        int y = yt.toInt();
        String color = colort.toString();
        Agent agent = getAgent(sAgent);
        agent.signalMove.emit();
        writeToLog("Agent entered: " + agent.getName());
        Point position = new Point(x, y);
        String pos = "(" + x + "," + y + ")";
        if (agent.isEntered()) {
            writeToLog("agent already entered");
            throw new ExternalActionFailedException("Agent \"" + agent.getName() + "\" has already entered.");
        }
        if (isOutOfBounds(position)) {
            throw new ExternalActionFailedException("Position " + pos + " out of bounds.");
        }
        if (!isFree(position)) {
            throw new ExternalActionFailedException("Position " + pos + " is not free.");
        }
        agent._position = position;
        int nColorID = getColorID(color);
        agent._colorID = nColorID;
        validatewindow();
        m_window.repaint();
        agent.signalMoveSucces.emit();
        return wrapBoolean(true);
    }
