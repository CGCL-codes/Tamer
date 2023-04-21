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
