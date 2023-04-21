    public void setNode(CVSFileNode node) {
        if (nodeHash != null) {
            nodeHash.setText("#" + node.hashCode());
        }
        file.setText(node.getName());
        file.setToolTipText(node.getFile().getAbsolutePath());
        String typeText = CVSFileTypeUtil.getFileSystemTypeDescriptionName(node.getFile());
        type.setText(((typeText == null) || typeText.equals("")) ? "File" : typeText);
        Icon icn = null;
        if (node.getFile().exists()) icn = CVSFileTypeUtil.getFileSystemIcon(node.getFile()); else icn = UIUtil.EMPTY_SMALL_ICON;
        type.setIcon(icn);
        long length = node.getFile().length();
        size.setToolTipText(String.valueOf(length) + " bytes");
        size.setText(GruntspudUtil.formatFileSize(length));
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
            locallyLastModified.setText(DATE_FORMAT.format(new Date(node.getFile().lastModified())));
        } else {
            locallyLastModified.setText("N/A");
        }
        Entry e = node.getEntry();
        FileStatus local = node.getLocalStatus();
        localStatus.setText(node.getLocalStatusText());
        localStatus.setIcon(node.getIconForStatus(local, node.getBaseIcon(false)));
        FileStatus remote = node.getRemoteStatus();
        remoteStatus.setText(node.getRemoteStatusText());
        remoteStatus.setIcon(node.getIconForStatus(remote, node.getBaseIcon(false)));
        if (e != null) {
            keywordSubstitution.setEnabled(true);
            remotelyLastModified.setEnabled(true);
            flags.setEnabled(true);
            localStatus.setEnabled(true);
            remoteStatus.setEnabled(true);
            tag.setEnabled(true);
            conflict.setEnabled(true);
            revision.setEnabled(true);
            CVSSubstType type = node.getCVSSubstType();
            keywordSubstitution.setIcon(type.getIcon());
            keywordSubstitution.setText(type.getName());
            if (e.getLastModified() != null) {
                remotelyLastModified.setText(DATE_FORMAT.format(e.getLastModified()));
                Date locDate = new Date(node.getFile().lastModified());
                long loc = node.getFile().lastModified();
                Date remoDate = e.getLastModified();
                long remo = remoDate.getTime();
                long dif = loc - remo;
                long tzDiff = TimeZone.getDefault().getRawOffset();
            } else {
                remotelyLastModified.setText("N/A");
            }
            flags.setText(e.getOptions());
            tag.setText(node.getTag());
            conflict.setText(e.getConflict());
            revision.setText(e.getRevision());
        } else {
            remotelyLastModified.setText("N/A");
            keywordSubstitution.setText("N/A");
            keywordSubstitution.setIcon(null);
            flags.setText("N/A");
            tag.setText("N/A");
            conflict.setText("N/A");
            revision.setText("N/A");
            keywordSubstitution.setEnabled(false);
            remotelyLastModified.setEnabled(false);
            flags.setEnabled(false);
            tag.setEnabled(false);
            conflict.setEnabled(false);
            revision.setEnabled(false);
        }
    }
