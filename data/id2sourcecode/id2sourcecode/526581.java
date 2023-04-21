    public void openFile(File file) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile newfile = null;
        try {
            newfile = new PDFFile(buf);
        } catch (IOException ioe) {
            openError(file.getPath() + " doesn't appear to be a PDF file.");
            return;
        }
        doClose();
        this.curFile = newfile;
        docName = file.getName();
        setTitle(TITLE + ": " + docName);
        if (doThumb) {
            thumbs = new PDFThumbPanel(curFile);
            thumbs.addPageChangeListener(this);
            thumbscroll.getViewport().setView(thumbs);
            thumbscroll.getViewport().setBackground(Color.gray);
        }
        setEnabling();
        forceGotoPage(0);
        try {
            outline = curFile.getOutline();
        } catch (IOException ioe) {
        }
        if (outline != null) {
            if (outline.getChildCount() > 0) {
                olf = new JDialog(this, "Outline");
                olf.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                olf.setLocation(this.getLocation());
                JTree jt = new JTree(outline);
                jt.setRootVisible(false);
                jt.addTreeSelectionListener(this);
                JScrollPane jsp = new JScrollPane(jt);
                olf.getContentPane().add(jsp);
                olf.pack();
                olf.setVisible(true);
            } else {
                if (olf != null) {
                    olf.setVisible(false);
                    olf = null;
                }
            }
        }
    }
