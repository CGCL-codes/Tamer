    public void service(final User user, final String channel, final Long roomid, final String passwd, final InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            OutputStream outputStream = user.getOutputStream();
            outputStream.write(("ConnectID:" + user.getUserid() + "\r\n").getBytes());
            outputStream.write(("ConnectName:" + user.getUsername() + "\r\n").getBytes());
            final Channel channel2 = ChipChat.getInstance().getChannel(channel);
            final Room room = channel2.getRoom(roomid);
            if (room == null) {
                outputStream.write("ERROR:RoomIsNotExist\r\n".getBytes());
                outputStream.flush();
                return;
            }
            int enter;
            if ("00admin".equals(user.getUsername()) && ("\t" + ADMINPW).equals(passwd)) {
                enter = room.enterAdmin(user, passwd);
            } else {
                enter = room.enterUser(user, passwd);
            }
            if (enter != 0) {
                if (enter == -1) {
                    outputStream.write("ERROR:RoomIsFull\r\n".getBytes());
                } else if (enter == -2) {
                    outputStream.write("ERROR:PasswordNotMatch\r\n".getBytes());
                } else {
                    outputStream.write("ERROR:Unknown\r\n".getBytes());
                }
                outputStream.flush();
                return;
            }
            outputStream.write(("Connected:" + SERVERNAME + "\r\n").getBytes());
            outputStream.write(("ADMIN:" + room.getMaster() + "\r\n").getBytes());
            outputStream.flush();
            boolean connected = true;
            room.inputRoomInfo();
            try {
                while (connected) {
                    String r = reader.readLine();
                    if (r == null) {
                        break;
                    }
                    try {
                        int index = r.indexOf(":");
                        if (index < 0) {
                            System.out.println("NO Command Found .... Skipping....:" + r);
                            continue;
                        }
                        String cmd = r.substring(0, index);
                        if (cmd.equalsIgnoreCase("MSG")) {
                            room.inputMsg(-1, r.substring(index + 1), user.getUsername());
                        } else if (cmd.equalsIgnoreCase("WSP")) {
                            try {
                                int index2 = r.indexOf(":", index + 1);
                                int to = Integer.parseInt(r.substring(index + 1, index2));
                                room.inputWhisper(user.getUserid().intValue(), to, r.substring(index2 + 1), user.getUsername());
                            } catch (NumberFormatException e) {
                                System.out.println("Wrong User ID:" + r);
                            }
                        } else if (cmd.equalsIgnoreCase("ACK")) {
                            outputStream.write("ACK:\r\n".getBytes());
                            outputStream.flush();
                            continue;
                        } else if (cmd.equalsIgnoreCase("KEEPQUIET")) {
                            try {
                                int to = Integer.parseInt(r.substring(index + 1));
                                room.inputKeepQuiet(user.getUserid().intValue(), to);
                            } catch (NumberFormatException e) {
                                System.out.println("Wrong User ID:" + r);
                            }
                            continue;
                        } else if (cmd.equalsIgnoreCase("KICKOUT")) {
                            try {
                                int to = Integer.parseInt(r.substring(index + 1));
                                room.inputKickOut(user.getUserid().intValue(), to);
                            } catch (NumberFormatException e) {
                                System.out.println("Wrong User ID:" + r);
                            }
                            continue;
                        } else if (cmd.equalsIgnoreCase("ENTRUST")) {
                            try {
                                int to = Integer.parseInt(r.substring(index + 1));
                                room.inputEntrust(user.getUserid().intValue(), to);
                            } catch (NumberFormatException e) {
                                System.out.println("Wrong User ID:" + r);
                            }
                            continue;
                        } else if (cmd.equalsIgnoreCase("GETOUT")) {
                            break;
                        } else if (cmd.equalsIgnoreCase("CHGPASSWORD")) {
                            String str = r.substring(index + 1);
                            room.changePasswd(user.getUserid().intValue(), str);
                        } else if (cmd.equalsIgnoreCase("CHGROOMNAME")) {
                            String str = r.substring(index + 1);
                            room.changeRoomName(user.getUserid().intValue(), str);
                        } else if (cmd.equalsIgnoreCase("CHGMAX")) {
                            try {
                                int num = Integer.parseInt(r.substring(index + 1));
                                room.changeMaxMan(user.getUserid().intValue(), num);
                            } catch (NumberFormatException e) {
                                System.out.println("Wrong Number Format:" + r);
                            }
                        } else if (cmd.equalsIgnoreCase("CUSTOM")) {
                            try {
                                int index2 = r.indexOf(":", index + 1);
                                int to = Integer.parseInt(r.substring(index + 1, index2));
                                room.inputCustomMsg(to, r.substring(index2 + 1), user.getUsername());
                            } catch (NumberFormatException e) {
                                System.out.println("Wrong Number Format:" + r);
                            }
                        } else {
                            System.out.println("Wrong Command.... Skipping....:" + r);
                            continue;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                room.exitUser(user);
                connected = false;
                try {
                    if (outputStream != null) {
                        outputStream.write("CLOSED:\r\n".getBytes());
                        outputStream.flush();
                    }
                } catch (IOException e) {
                    System.err.println("IOException : 203");
                    e.printStackTrace();
                }
            }
            System.out.println("Listen thread ended....[" + user.getUserid() + "]");
        } catch (IOException e) {
            System.err.println("IOException : 155");
            e.printStackTrace();
        }
    }
