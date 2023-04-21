    public boolean reselect(String folderName) {
        long start = System.currentTimeMillis();
        boolean oldEcho = echo;
        if (Editor.isDebugEnabled()) echo = true;
        try {
            if (state < AUTHENTICATED || !writeTagged("select \"" + folderName + "\"")) {
                connect();
                if (state < AUTHENTICATED) return false;
                if (!writeTagged("select \"" + folderName + "\"")) return false;
            }
            while (true) {
                String s = readLine();
                if (s == null) {
                    Log.error("ImapSession.reselect readLine returned null");
                    this.folderName = null;
                    messageCount = 0;
                    recent = 0;
                    return false;
                }
                final String upper = s.toUpperCase();
                if (upper.startsWith("* NO ")) {
                    mailbox.setStatusText(s.substring(5).trim());
                    continue;
                }
                if (upper.startsWith("* ")) {
                    if (upper.endsWith(" EXISTS")) {
                        processUntaggedResponse(s);
                        continue;
                    }
                    if (upper.endsWith(" RECENT")) {
                        processUntaggedResponse(s);
                        continue;
                    }
                }
                if (upper.startsWith(UIDVALIDITY)) {
                    uidValidity = Utilities.parseInt(s.substring(UIDVALIDITY.length()));
                    continue;
                }
                if (upper.startsWith(UIDNEXT)) {
                    uidNext = Utilities.parseInt(s.substring(UIDNEXT.length()));
                    continue;
                }
                if (upper.startsWith(lastTag + " ")) {
                    if (upper.startsWith(lastTag + " OK ")) {
                        state = SELECTED;
                        this.folderName = folderName;
                        readOnly = upper.indexOf("[READ-ONLY]") >= 0;
                        if (readOnly) {
                            Log.warn("reselect mailbox " + folderName + " is read-only!");
                            setLastErrorMillis(System.currentTimeMillis());
                        } else {
                            Log.debug("reselect mailbox " + folderName + " is read-write");
                        }
                        return true;
                    } else {
                        Log.error("SELECT " + folderName + " failed");
                        state = AUTHENTICATED;
                        this.folderName = null;
                        messageCount = 0;
                        recent = 0;
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            Log.error(e);
            disconnect();
            this.folderName = null;
            messageCount = 0;
            recent = 0;
            return false;
        } finally {
            echo = oldEcho;
            long elapsed = System.currentTimeMillis() - start;
            Log.debug("ImapSession.reselect " + folderName + " " + elapsed + " ms");
        }
    }
