    private final void showMessage(Client clientI, com.rbnb.sapi.ChannelMap messageI, boolean refreshI) {
        String sender;
        String message;
        double[][] times = new double[messageI.NumberOfChannels()][];
        int[] index = new int[messageI.NumberOfChannels()];
        for (int idx = 0; idx < messageI.NumberOfChannels(); ++idx) {
            times[idx] = messageI.GetTimes(idx);
            index[idx] = 0;
        }
        boolean done = false;
        double wtime, ctime;
        int cIdx;
        boolean updateUsers = false;
        while (!done) {
            done = true;
            cIdx = -1;
            wtime = Double.MAX_VALUE;
            for (int idx = 0; idx < messageI.NumberOfChannels(); ++idx) {
                if (index[idx] < times[idx].length) {
                    done = false;
                    ctime = times[idx][index[idx]];
                    if ((cIdx == -1) || (ctime < wtime)) {
                        cIdx = idx;
                        wtime = ctime;
                    }
                }
            }
            if (!done && (cIdx >= 0)) {
                sender = messageI.GetName(cIdx);
                sender = sender.substring(clientI.getHostName().length() + 1 + clientI.getChatRoom().length());
                if (!updateUsers) {
                    if (sidePanel.chatUserList == null) {
                        updateUsers = true;
                    } else {
                        int lo = 0, hi = sidePanel.chatUserList.length - 1, idx1;
                        updateUsers = true;
                        for (int idx = (lo + hi) / 2; updateUsers && (lo <= hi); idx = (lo + hi) / 2) {
                            idx1 = sender.compareTo(sidePanel.chatUserList[idx]);
                            if (idx1 == 0) {
                                updateUsers = false;
                            } else if (idx1 < 0) {
                                hi = idx1 - 1;
                            } else {
                                lo = idx1 + 1;
                            }
                        }
                    }
                }
                String say;
                int count = 1;
                if (messageI.GetType(cIdx) == com.rbnb.sapi.ChannelMap.TYPE_STRING) {
                    say = messageI.GetDataAsString(cIdx)[index[cIdx]];
                } else {
                    count = messageI.GetTimes(cIdx).length;
                    say = ("Binary data message - " + count + " " + DATA_TYPES[messageI.GetType(cIdx)] + ((count > 1) ? "s." : "."));
                }
                String time = formatDisplayTime(com.rbnb.api.Time.since1970(wtime));
                String serv = null;
                String s = sender;
                int idx1 = s.lastIndexOf("/");
                s = s.substring(idx1 + 1);
                serv = "<" + s + ">: ";
                if (say.startsWith("\n")) {
                    say = say.substring(1);
                }
                if (say.endsWith("\n")) {
                    say = say.substring(0, say.length() - 1);
                }
                String disp = time + serv + say + "\n";
                report(disp);
                if (log != null) {
                    log.print(disp);
                    log.flush();
                }
                if (!refreshI) {
                    if (beepOnNew) {
                        defTool.beep();
                    }
                    if (say.startsWith("ALERT")) {
                        showAlert(time, serv, say);
                    }
                }
                index[cIdx] += count;
            }
            if (updateUsers) {
                sidePanel.updateUsers();
            }
        }
    }
