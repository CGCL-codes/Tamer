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
        if (doThumb) {
            thumbs = new ThumbPanel(curFile);
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
                JTree jt = new JTree(outline);
                jt.setRootVisible(false);
                jt.addTreeSelectionListener(this);
                JScrollPane jsp = new JScrollPane(jt);
                outlinePanel.removeAll();
                outlinePanel.add(jsp);
                this.revalidate();
            } else {
                if (olf != null) {
                    olf.setVisible(false);
                    olf = null;
                }
            }
        }
    }
