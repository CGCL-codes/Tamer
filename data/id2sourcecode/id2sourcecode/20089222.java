    @TodoList({ @Todo(desc = "Create an \"ignore exception in this block\" utility -- avoid \"empty block\" warnings and demonstrate intent"), @Todo(desc = "Refactor this method in to smaller pieces") })
    @Override()
    public void run() {
        int largestCommandLength = 0;
        for (TrancheServerCommandLineClientItem dfsclci : items.values()) {
            if (dfsclci.getName().length() > largestCommandLength) {
                largestCommandLength = dfsclci.getName().length();
            }
        }
        StringWriter sw = new StringWriter();
        out.println("Tranche server command line client started. \"?\" lists commands.");
        while (isRun()) {
            try {
                if (in.equals(System.in)) {
                    if (System.getProperty("noAutoReboot") != null) {
                        sw.write(br.readLine());
                    } else {
                        while (isRun()) {
                            try {
                                if (in.available() > 0) {
                                    int c = in.read();
                                    if (c == '\n') {
                                        break;
                                    }
                                    sw.write((char) c);
                                }
                                try {
                                    Thread.sleep(100);
                                } catch (Exception e) {
                                }
                            } catch (Exception e) {
                                try {
                                    Thread.sleep(500);
                                } catch (Exception ee) {
                                }
                            }
                        }
                        if (!isRun()) {
                            return;
                        }
                    }
                } else {
                    for (int c = in.read(); c != '\n'; c = in.read()) {
                        if (c == -1) {
                            return;
                        }
                        sw.write((char) c);
                    }
                }
                String command = sw.toString().trim();
                sw = new StringWriter();
                String[] commandParts = TrancheServerCommandLineClientItem.splitCommandLine(command);
                if (command.equalsIgnoreCase("?") || command.equals("")) {
                    int pad = 2;
                    out.setPadding(largestCommandLength + pad);
                    out.print("  Command");
                    for (int i = 0; i < largestCommandLength - "Command".length(); i++) {
                        out.print(" ");
                    }
                    out.println("Description");
                    for (int i = 0; i < out.getMaxCharactersPerLine(); i++) {
                        out.print("-");
                    }
                    out.println("");
                    TrancheServerCommandLineClientItem[] list = items.values().toArray(new TrancheServerCommandLineClientItem[0]);
                    Arrays.sort(list);
                    for (TrancheServerCommandLineClientItem item : list) {
                        out.print(item.getName());
                        for (int i = 0; i < largestCommandLength - item.getName().length(); i++) {
                            out.print(" ");
                        }
                        for (int i = 0; i < pad; i++) {
                            out.print(" ");
                        }
                        out.println(item.getDescription());
                        out.println("");
                    }
                    out.setPadding(0);
                    continue;
                }
                TrancheServerCommandLineClientItem dfsclci = null;
                for (String itemName : items.keySet()) {
                    if (itemName.equals(commandParts[0])) {
                        dfsclci = items.get(itemName);
                        break;
                    }
                }
                if (dfsclci == null) {
                    for (String itemName : items.keySet()) {
                        if (itemName.startsWith(commandParts[0])) {
                            dfsclci = items.get(itemName);
                            break;
                        }
                    }
                }
                if (dfsclci != null) {
                    dfsclci.preDoAction(command, new BufferedReader(new InputStreamReader(in)), out);
                    out.flush();
                    continue;
                }
                out.println(command + " is not a valid command.");
            } catch (Exception e) {
                debugErr(e);
            }
        }
    }
