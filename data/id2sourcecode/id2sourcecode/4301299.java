    public void open(File f) throws IOException {
        this.file = f;
        IsoFile isoFile = new IsoFile(new RandomAccessFile(f, "r").getChannel());
        long start = System.nanoTime();
        final List<LogRecord> messages = new LinkedList<LogRecord>();
        Handler myTemperaryLogHandler = new Handler() {

            @Override
            public void publish(LogRecord record) {
                messages.add(record);
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        };
        Logger.getLogger("").addHandler(myTemperaryLogHandler);
        Logger.getAnonymousLogger().removeHandler(myTemperaryLogHandler);
        System.err.println("Parsing took " + ((System.nanoTime() - start) / 1000000d) + "ms.");
        Path oldMp4Path = new Path((IsoFile) tree.getModel().getRoot());
        tree.setModel(new IsoFileTreeModel(isoFile));
        tree.revalidate();
        Path nuMp4Path = new Path(isoFile);
        trackList.setModel(new TrackListModel(isoFile));
        if (!messages.isEmpty()) {
            String message = "";
            for (LogRecord logRecord : messages) {
                message += logRecord.getMessage() + "\n";
            }
            JOptionPane.showMessageDialog(this, message, "Parser Messages", JOptionPane.WARNING_MESSAGE);
        }
        if (details instanceof Box && oldMp4Path != null) {
            String path = oldMp4Path.createPath((Box) details);
            Box nuDetail = nuMp4Path.getPath(path);
            if (nuDetail != null) {
                showDetails(nuDetail);
            } else {
                showDetails(isoFile);
            }
        } else {
            showDetails(isoFile);
        }
        mainFrame.setTitle("Iso Viewer - " + f.getAbsolutePath());
    }
