        @Override
        public void run() {
            log.info("Reader thread starting");
            log.info("TC input stream " + is);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            log.info("Buffered reader " + br);
            String line = null;
            try {
                line = br.readLine();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            do {
                try {
                    log.info("Reader thread got line >" + line + "<");
                    if (line.startsWith("LOGIN: login successful")) {
                        synchronized (loginState) {
                            loginState.loggedIn = true;
                            loginState.invalidLogin = false;
                            loginState.notify();
                        }
                        queue.priorityAdd(new LutronCommand("PROMPTOFF", null, null));
                        queue.priorityAdd(new LutronCommand("DLMON", null, null));
                        queue.priorityAdd(new LutronCommand("GSMON", null, null));
                        queue.priorityAdd(new LutronCommand("KLMON", null, null));
                    } else if (line.startsWith("LOGIN:")) {
                        log.info("Asked to login, wakening writer thread");
                        synchronized (loginState) {
                            loginState.loggedIn = false;
                            loginState.needsLogin = true;
                            loginState.notify();
                        }
                    } else if (line.startsWith("login incorrect")) {
                        synchronized (loginState) {
                            loginState.loggedIn = false;
                            loginState.invalidLogin = true;
                        }
                    } else if (line.startsWith("closing connection")) {
                        synchronized (loginState) {
                            loginState.loggedIn = false;
                        }
                        break;
                    } else {
                        LutronResponse response = parseResponse(line);
                        if (response != null) {
                            if ("GSS".equals(response.response)) {
                                try {
                                    GrafikEye ge = (GrafikEye) getHomeWorksDevice(response.address, GrafikEye.class);
                                    if (ge != null) {
                                        ge.processUpdate(response.parameter);
                                    }
                                } catch (LutronHomeWorksDeviceException e) {
                                    log.error("Impossible to get device", e);
                                }
                            } else if ("KLS".equals(response.response)) {
                                try {
                                    Keypad keypad = (Keypad) getHomeWorksDevice(response.address, Keypad.class);
                                    if (keypad != null) {
                                        keypad.processUpdate(response.parameter);
                                    }
                                } catch (LutronHomeWorksDeviceException e) {
                                    log.error("Impossible to get device", e);
                                }
                            } else if ("DL".equals(response.response)) {
                                try {
                                    Dimmer dim = (Dimmer) getHomeWorksDevice(response.address, Dimmer.class);
                                    if (dim != null) {
                                        dim.processUpdate(response.parameter);
                                    }
                                } catch (LutronHomeWorksDeviceException e) {
                                    log.error("Impossible to get device", e);
                                }
                            }
                        } else {
                        }
                    }
                    line = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (line != null && !isInterrupted());
        }
