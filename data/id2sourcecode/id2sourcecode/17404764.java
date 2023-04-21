    private void onChanMode(String host, String channel, String mode) {
        boolean plus = true;
        String[] list = mode.split(" ");
        char modeChar;
        String modePiece = "";
        int param = 1;
        Channel chan = getChannel(channel);
        final String pmArgs = "ohvbeI";
        final String pArgs = "lk";
        try {
            for (int index = 0; index < list[0].length(); index++) {
                modeChar = list[0].charAt(index);
                if (modeChar == '+') {
                    plus = true;
                } else if (modeChar == '-') {
                    plus = false;
                } else if (pmArgs.indexOf("" + modeChar) >= 0) {
                    modePiece = modeChar + " " + list[param++];
                } else if (pArgs.indexOf("" + modeChar) >= 0) {
                    if (plus) {
                        modePiece = modeChar + " " + list[param++];
                    } else {
                        modePiece = "" + modeChar;
                    }
                } else {
                    modePiece = "" + modeChar;
                }
                if (plus && modeChar != '+' && modeChar != '-') {
                    modePiece = "+" + modePiece;
                } else {
                    modePiece = "-" + modePiece;
                }
                if (modeChar != '+' && modeChar != '-') {
                    char flag = modePiece.charAt(1);
                    if (modePiece.indexOf(" ") >= 0) {
                        String arg = modePiece.split(" ")[1];
                        plus = modePiece.charAt(0) == '+';
                        if (flag == 'o') {
                            if (plus) {
                                if (arg.equals(nick)) {
                                    chan.setMyOp(true);
                                } else {
                                    chan.addOp(getUser(arg));
                                }
                            } else {
                                if (arg.equals(nick)) {
                                    chan.setMyOp(false);
                                } else {
                                    chan.delOp(arg);
                                }
                            }
                        } else if (flag == 'h') {
                            if (plus) {
                                if (arg.equals(nick)) {
                                    chan.setMyHop(true);
                                } else {
                                    chan.addHop(getUser(arg));
                                }
                            } else {
                                if (arg.equals(nick)) {
                                    chan.setMyHop(false);
                                } else {
                                    chan.delHop(arg);
                                }
                            }
                        } else if (flag == 'v') {
                            if (plus) {
                                if (arg.equals(nick)) {
                                    chan.setMyVoice(true);
                                } else {
                                    chan.addVoice(getUser(arg));
                                }
                            } else {
                                if (arg.equals(nick)) {
                                    chan.setMyVoice(false);
                                } else {
                                    chan.delVoice(arg);
                                }
                            }
                        } else if (flag == 'k') {
                            chan.setKey(arg);
                        } else if (flag == 'l') {
                            chan.setLimit(arg);
                        }
                    } else {
                        if (modePiece.charAt(0) == '+') {
                            chan.setModeFlag(flag);
                        } else {
                            if (flag == 'k') {
                                chan.clearKey();
                            } else if (flag == 'l') {
                                chan.clearLimit();
                            } else {
                                chan.clearModeFlag(flag);
                            }
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            libairc.debug("Mode", "Channel " + channel + " received invalid mode string " + mode + ".");
        }
    }
