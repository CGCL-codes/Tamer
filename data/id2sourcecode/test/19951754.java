    public void run() {
        String pass = properties.getProperty("pass", "");
        ServerSocketChannel server = null;
        try {
            selector = Selector.open();
            server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress(port));
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
            DecimalFormat decimal = (DecimalFormat) DecimalFormat.getInstance();
            decimal.applyPattern("#.##");
            if (verbose) out.println("daemon started\n" + "- pass       \t" + pass + "\n" + "- port       \t" + port + "\n" + "- worker(s)  \t" + threads + " thread" + (threads > 1 ? "s" : "") + "\n" + "- session    \t" + cookie + " characters\n" + "- timeout    \t" + decimal.format((double) timeout / 60000) + " minute" + (timeout / 60000 > 1 ? "s" : "") + "\n" + "- IO timeout \t" + delay + " ms." + "\n" + "- IO buffer  \t" + size + " bytes\n" + "- debug      \t" + debug + "\n" + "- live       \t" + properties.getProperty("live", "false").toLowerCase().equals("true"));
            if (pass != null && pass.length() > 0 || host) {
                if (host) {
                    add(new Deploy("app" + File.separator));
                } else {
                    add(new Deploy("app" + File.separator, pass));
                }
                File[] app = new File(Deploy.path).listFiles(new Filter());
                if (app != null) {
                    for (int i = 0; i < app.length; i++) {
                        Deploy.deploy(this, app[i]);
                    }
                }
            }
            if (panel) {
                add(new Service() {

                    public String path() {
                        return "/panel";
                    }

                    public void filter(Event event) throws Event, Exception {
                        Iterator it = workers.iterator();
                        event.output().println("<pre>workers: {size: " + workers.size() + ", ");
                        while (it.hasNext()) {
                            Worker worker = (Worker) it.next();
                            event.output().print(" worker: {index: " + worker.index() + ", busy: " + worker.busy() + ", lock: " + worker.lock());
                            if (worker.event() != null) {
                                event.output().println(", ");
                                event.output().println("  event: {index: " + worker.event() + ", init: " + worker.event().reply().output.init + ", done: " + worker.event().reply().output.done + "}");
                                event.output().println(" }");
                            } else {
                                event.output().println("}");
                            }
                        }
                        event.output().println("}");
                        event.output().println("events: {size: " + events.size() + ", selected: " + selected + ", valid: " + valid + ", accept: " + accept + ", readwrite: " + readwrite + ", ");
                        it = events.values().iterator();
                        while (it.hasNext()) {
                            Event e = (Event) it.next();
                            event.output().println(" event: {index: " + e + ", last: " + (System.currentTimeMillis() - e.last()) + "}");
                        }
                        event.output().println("}</pre>");
                    }
                });
            }
            if (properties.getProperty("test", "false").toLowerCase().equals("true")) {
                new Test(this, 1);
            }
        } catch (Exception e) {
            e.printStackTrace(out);
            System.exit(1);
        }
        int index = 0;
        Event event = null;
        SelectionKey key = null;
        while (alive) {
            try {
                selector.select();
                Set set = selector.selectedKeys();
                int valid = 0, accept = 0, readwrite = 0, selected = set.size();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    key = (SelectionKey) it.next();
                    it.remove();
                    if (key.isValid()) {
                        valid++;
                        if (key.isAcceptable()) {
                            accept++;
                            event = new Event(this, key, index++);
                            events.put(new Integer(event.index()), event);
                            if (Event.LOG) {
                                event.log("accept ---");
                            }
                        } else if (key.isReadable() || key.isWritable()) {
                            readwrite++;
                            key.interestOps(0);
                            event = (Event) key.attachment();
                            Worker worker = event.worker();
                            if (Event.LOG) {
                                if (debug) {
                                    if (key.isReadable()) event.log("read ---");
                                    if (key.isWritable()) event.log("write ---");
                                }
                            }
                            if (key.isReadable() && event.push()) {
                                event.disconnect(null);
                            } else if (worker == null) {
                                employ(event);
                            } else {
                                worker.wakeup();
                            }
                        }
                    }
                }
                this.valid = valid;
                this.accept = accept;
                this.readwrite = readwrite;
                this.selected = selected;
            } catch (Exception e) {
                if (event == null) {
                    System.out.println(events + " " + key);
                } else {
                    event.disconnect(e);
                }
            }
        }
        try {
            if (selector != null) {
                selector.close();
            }
            if (server != null) {
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace(out);
        }
    }
