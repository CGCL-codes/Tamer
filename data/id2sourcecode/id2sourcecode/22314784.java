    public void run() {
        int read;
        byte[] str_login = (my_login + "\n").getBytes();
        byte[] str_pass = (my_pass + "\n").getBytes();
        boolean login_prompted = false;
        byte buf[] = new byte[1024 * 4];
        if (interact) this.requestFocus();
        synced = true;
        while (true) {
            try {
                receive.sleep(100);
                if ((read = sIn.read(buf)) >= 0) {
                    write(buf, 0, read);
                    if (conf_login) {
                        if (read > 2) {
                            conf_login = false;
                            if (Search_word(buf, "incorrect", read) == -1) {
                                logged = true;
                                if (main_win != null) main_win.telnet_thread_logged = true;
                            } else {
                                logged = false;
                                if (main_win != null) main_win.telnet_thread_logged = false;
                                try {
                                    throw new Exception("login failed");
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                    if (!logged) {
                        if (!login_prompted) {
                            if (Search_word(buf, "login:", read) >= 0) {
                                login_prompted = true;
                                sOut.write(str_login);
                            }
                        } else {
                            if (Search_word(buf, "assword:", read) >= 0) {
                                sOut.write(str_pass);
                                if (main_win != null) main_win.telnet_thread_logged = false;
                                conf_login = true;
                                synced = true;
                            }
                        }
                    } else {
                        if (!ready) {
                            if (interact) {
                                sOut.write("clear\n".getBytes());
                                this.requestFocus();
                            }
                            ready = true;
                        }
                        if (!synced && !interact) {
                            if ((to_search != null) && (!to_search.isEmpty())) {
                                if (Search_word(buf, to_search, read) >= 0) {
                                    lock = false;
                                    synced = true;
                                    if (to_be_notified != null) {
                                        ActionEvent actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "telnet_synced");
                                        String target = to_be_notified.getClass().toString();
                                        if (target.contains("win_flash")) {
                                            win_flash qualified = (win_flash) to_be_notified;
                                            qualified.actionPerformed(actionEvent);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (read == -1) {
                    if (interact) this.getParent().setVisible(false);
                    break;
                }
            } catch (IOException e) {
                System.out.println("Cought Something(IO)");
            } catch (InterruptedException e) {
                System.out.println("Cought Something(Interrupted)");
            }
        }
    }
