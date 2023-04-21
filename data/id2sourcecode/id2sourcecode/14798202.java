    private static void filterMessage(final Message m) {
        if (!main.getDisplay().isDisposed()) {
            main.getDisplay().asyncExec(new Runnable() {

                public void run() {
                    Room r = m.getBot().getConnection().findRoom(m.getChannel());
                    if (r == null) return;
                    if (Settings.getSettings().getNicksIgnored().contains(m.getSender())) return;
                    String formattedMsg;
                    if (m.getType() == Message.ACTION) formattedMsg = Settings.getSettings().getActionFormat(); else formattedMsg = Settings.getSettings().getMessageFormat();
                    formattedMsg = formattedMsg.replaceAll("%chan%", m.getChannel());
                    formattedMsg = formattedMsg.replaceAll("%msg%", m.getContent());
                    formattedMsg = formattedMsg.replaceAll("%nick%", m.getSender());
                    SimpleDateFormat sdf = new SimpleDateFormat(Settings.getSettings().getTimestampFormatPattern());
                    if (Settings.getSettings().isTimestampsEnabled()) formattedMsg = formattedMsg.replaceAll("%time%", sdf.format(m.getDate())); else formattedMsg = formattedMsg.replaceAll("%time%", "");
                    if (m.getType() != Message.MSG) {
                        String colorStr = Settings.getSettings().getOutputColors().get(m.getType());
                        formattedMsg = colorset.ircColorsStr.get(colorStr) + formattedMsg;
                    }
                    String strippedLine = Colors.removeFormattingAndColors(formattedMsg);
                    if (r.getOutput() != null) {
                        int scrollPos = r.getOutput().getTopPixel();
                        int ySize = r.getOutput().getBounds().height;
                        boolean scrollDown = (scrollPos > (r.getOutput().getVerticalBar().getMaximum() - ySize));
                        switch(m.getType()) {
                            case Message.ACTION:
                            case Message.MSG:
                            case Message.PM:
                            case Message.NOTICE:
                                sendMessageToRoom(m, r, strippedLine);
                                break;
                            case Message.CONSOLE:
                                r.newMessage(strippedLine);
                                r.changeStatus(Room.NEW_IRC_EVENT);
                                break;
                        }
                        if (scrollDown) r.getOutput().setSelection(r.getOutput().getText().length());
                    }
                    List<StyleRange> styleRanges = ControlCodeParser.parseControlCodes(formattedMsg, r.getOutput().getText().length() - strippedLine.length());
                    for (StyleRange styleRange : styleRanges.toArray(new StyleRange[styleRanges.size()])) r.getOutput().setStyleRange(styleRange);
                    for (String s : strippedLine.split(" ")) {
                        if (s.contains("://") || Quicklinks.hasQuicklink(s)) {
                            linkify(r, strippedLine, s);
                        }
                    }
                }

                private void sendMessageToRoom(final Message m, Room r, String strippedLine) {
                    r.newMessage(strippedLine, true);
                    if (strippedLine.toLowerCase().contains(m.getBot().getNick().toLowerCase())) r.changeStatus(Room.NAME_CALLED); else r.changeStatus(Room.NEW_MESSAGE);
                }

                private void linkify(Room r, String strippedLine, String s) {
                    Color blue = new Color(r.getOutput().getDisplay(), 0, 0, 255);
                    StyleRange styleRange = new StyleRange();
                    styleRange.start = r.getOutput().getCharCount() - strippedLine.length() + strippedLine.indexOf(s);
                    styleRange.length = s.length();
                    styleRange.foreground = blue;
                    styleRange.data = s;
                    styleRange.underline = true;
                    styleRange.underlineStyle = SWT.UNDERLINE_LINK;
                    r.getOutput().setStyleRange(styleRange);
                }
            });
        }
    }
