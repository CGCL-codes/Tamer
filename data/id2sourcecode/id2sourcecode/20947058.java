    private synchronized void read(ProgressNotifier progressNotifier) {
        Log.debug("entering Mbox.read");
        long start = System.currentTimeMillis();
        Debug.assertTrue(isLocked());
        entries = new ArrayList(1000);
        long messageStart = 0;
        MailReader reader = null;
        try {
            if (!file.isFile()) return;
            reader = new MailReader(file.getInputStream());
            FastStringBuffer sb = new FastStringBuffer(1024);
            boolean complete = false;
            while (true) {
                long here = reader.getOffset();
                String text = reader.readLine();
                if (text == null) {
                    Log.debug("read - end of file");
                    if (entries.size() > 0) {
                        LocalMailboxEntry entry = (LocalMailboxEntry) entries.get(entries.size() - 1);
                        entry.setSize((int) (here - messageStart));
                        entry.setNextMessageStart(here);
                    }
                    complete = true;
                    break;
                }
                if (text.startsWith("From ")) {
                    if (entries.size() > 0) {
                        LocalMailboxEntry entry = (LocalMailboxEntry) entries.get(entries.size() - 1);
                        entry.setSize((int) (here - messageStart));
                        entry.setNextMessageStart(here);
                        messageStart = here;
                    }
                    if (progressNotifier != null && progressNotifier.cancelled()) {
                        Log.debug("Mbox.read cancelled!");
                        break;
                    }
                    sb.setLength(0);
                    while (true) {
                        text = reader.readLine();
                        if (text == null) return;
                        if (text.length() == 0) break;
                        sb.append(text);
                        sb.append('\n');
                    }
                    LocalMailboxEntry entry = new LocalMailboxEntry(entries.size() + 1, here, sb.toString());
                    entries.add(entry);
                    if (progressNotifier != null) {
                        sb.setLength(0);
                        sb.append("Read ");
                        sb.append(entries.size());
                        sb.append(" message");
                        if (entries.size() > 1) sb.append('s');
                        progressNotifier.progress(sb.toString());
                    }
                }
            }
            if (complete) {
                long elapsed = System.currentTimeMillis() - start;
                Log.debug("Mbox.read " + elapsed + " ms");
                writeSummary();
                Log.debug("Mbox.read - after writeSummary");
                lastModified = file.lastModified();
            }
        } catch (IOException e) {
            Log.error(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.error(e);
                }
            }
            Log.debug("leaving Mbox.read");
        }
    }
