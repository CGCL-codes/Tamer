    public void setNode(CVSFileNode node) {
        this.node = node;
        directory.setText(node.getName());
        directory.setToolTipText(node.getFile().getAbsolutePath());
        String typeText = CVSFileTypeUtil.getFileSystemTypeDescriptionName(node.getFile());
        type.setText(((typeText == null) || typeText.equals("")) ? "File" : typeText);
        type.setIcon(node.getIcon(false));
        String accessText = null;
        Color accessColor = null;
        String modeText = null;
        if (!node.getFile().exists()) {
            accessText = "Doesn't exist";
            accessColor = Color.red;
        } else {
            GruntspudFileMode mode = GruntspudFileMode.getFileModeForFile(node.getFile());
            modeText = mode.toString();
            if (!node.getFile().canRead() && !node.getFile().canWrite()) {
                accessText = "No read / No write";
                accessColor = Color.red;
            } else if (!node.getFile().canWrite() && node.getFile().canRead()) {
                accessText = "Read only";
                accessColor = Color.red;
            } else if (node.getFile().canWrite() && !node.getFile().canRead()) {
                accessText = "Write only";
                accessColor = Color.red;
            } else if (node.getFile().canWrite() && node.getFile().canRead()) {
                accessText = "Read / Write";
            }
        }
        access.setText(accessText + ((modeText == null) ? "" : (" (" + modeText + ")")));
        access.setForeground((accessColor != null) ? accessColor : UIManager.getColor("Label.foreground"));
        if (node.getFile().exists()) {
            lastModified.setText(DATE_FORMAT.format(new Date(node.getFile().lastModified())));
        } else {
            lastModified.setText("N/A");
        }
        setSizes();
    }
