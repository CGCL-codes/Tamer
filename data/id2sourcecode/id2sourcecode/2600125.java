    public Socket connect(Socket listener) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(listener.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(listener.getOutputStream()));
            String line;
            while (true) {
                bw.write("> ");
                bw.flush();
                line = br.readLine();
                if (line == null) return null;
                line = line.trim() + " ";
                if (line.matches(".*[\0-\10\12-\37].*")) {
                    bw.write("Control characters detected, ignoring line.\r\n");
                    continue;
                }
                if (line.length() == 0) continue;
                char cmd;
                String args;
                if (shortCommands.indexOf(line.charAt(0)) != -1) {
                    cmd = line.charAt(0);
                    args = line.substring(1).trim();
                } else {
                    int pos = line.indexOf(' ');
                    args = line.substring(pos).trim();
                    pos = longCommands.indexOf(line.substring(0, pos));
                    if (pos == -1) continue;
                    cmd = shortCommands.charAt(pos);
                }
                if (allowedCommands.indexOf(cmd) == -1) continue;
                switch(cmd) {
                    case '?':
                        bw.write("Supported commands:\r\n\r\n");
                        bw.write("\tShort\tLong\tDescription\r\n");
                        bw.write("\t=====\t====\t===========\r\n\r\n");
                        for (int i = 0; i < shortCommands.length(); i++) {
                            if (allowedCommands.indexOf(shortCommands.charAt(i)) != -1) bw.write("\t" + shortCommands.charAt(i) + "\t" + longCommands.get(i) + "\t" + commandDescriptions[i] + "\r\n");
                        }
                        bw.write("\r\nShort commands do not need a space before their argument, long commands do.\r\n");
                        break;
                    case 'q':
                        return null;
                    case '!':
                        System.exit(10);
                        break;
                    case '.':
                        synchronized (logBuffer) {
                            byte[] result = logBuffer.toByteArray();
                            logBuffer.reset();
                            bw.write(new String(result) + "\r\n");
                        }
                        break;
                    case '=':
                        if (readOnlyRules.size() > 0) {
                            bw.write("Readonly rules:\r\n");
                            for (int i = 0; i < readOnlyRules.size(); i++) {
                                bw.write("\t" + readOnlyRules.get(i) + "\r\n");
                            }
                            bw.write("\r\n");
                        }
                        bw.write("Rules:\r\n");
                        for (int i = 0; i < rules.size(); i++) {
                            bw.write("  [" + i + "]\t" + rules.get(i) + "\r\n");
                        }
                        break;
                    case '+':
                        if (args.indexOf("Console@!") != -1) {
                            bw.write("Error: Added commands may not contain \"Console@!\".\r\n");
                        } else {
                            rules.add(args);
                            bw.write("Added.\r\n");
                            saveRules();
                        }
                        break;
                    case '-':
                        try {
                            int idx = Integer.parseInt(args);
                            if (idx >= 0 && idx < rules.size()) {
                                rules.remove(idx);
                                bw.write("Removed.\r\n");
                                saveRules();
                            }
                        } catch (NumberFormatException ex) {
                        }
                        break;
                    case '#':
                        try {
                            int idx = Integer.parseInt(args);
                            if (idx >= 0 && idx < rules.size()) {
                                String rule = (String) rules.get(idx);
                                String[] parts = rule.split("[ \t]+");
                                if (parts.length == 2) {
                                    ForwarderThread t = new ForwarderThread(parts[0], parts[1]);
                                    startedForwarderThreads.add(t);
                                    t.start();
                                }
                            }
                        } catch (NumberFormatException ex) {
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    default:
                        throw new RuntimeException("Invalid command: " + cmd);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
