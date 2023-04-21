    public static void openMailbox(Editor editor, MailboxURL url) {
        String limitPattern = url.getLimitPattern();
        Log.debug("limitPattern = |" + limitPattern + "|");
        MailboxFilter filter = null;
        boolean badLimitPattern = false;
        if (limitPattern != null) {
            filter = MailboxFilter.getMailboxFilter(limitPattern);
            if (filter == null) {
                MessageDialog.showMessageDialog("Bad limit pattern", "Open Mailbox");
                limitPattern = null;
                badLimitPattern = true;
            }
        }
        if (url instanceof ImapURL || url instanceof PopURL) {
            Mailbox mb = getMailbox(editor, url);
            if (mb != null) {
                if (mb.isLoaded()) {
                    if (filter != null && mb.getLimitFilter() == null) {
                        mb.limit(filter);
                        mb.setLimitPattern(limitPattern);
                    } else if (!badLimitPattern && mb == Editor.currentEditor().getBuffer()) {
                        mb.limit(filter);
                        mb.setLimitPattern(limitPattern);
                    }
                } else {
                    mb.setLimitFilter(filter);
                    mb.setLimitPattern(limitPattern);
                }
                editor.makeNext(mb);
                editor.switchToBuffer(mb);
                FolderTreeModel.getDefaultModel().maybeAddNodeForFolder(url);
                IdleThread idleThread = IdleThread.getInstance();
                if (idleThread != null) {
                    idleThread.maybeAddTask(CheckMailTask.getInstance());
                    if (mb instanceof PopMailbox) idleThread.maybeAddTask(RewriteMailboxesTask.getInstance());
                }
            }
        } else {
            Debug.assertTrue(url instanceof LocalMailboxURL);
            final File file = ((LocalMailboxURL) url).getFile();
            Mailbox mb = null;
            for (BufferIterator it = new BufferIterator(); it.hasNext(); ) {
                Buffer buf = it.nextBuffer();
                if (buf instanceof LocalMailbox) {
                    if (((LocalMailbox) buf).getMailboxFile().equals(file)) {
                        mb = (LocalMailbox) buf;
                        break;
                    }
                } else if (buf instanceof Drafts) {
                    if (((Drafts) buf).getDirectory().equals(file)) {
                        mb = (Drafts) buf;
                        break;
                    }
                }
            }
            if (mb == null) {
                if (file.equals(Directories.getDraftsFolder())) mb = new Drafts((LocalMailboxURL) url); else mb = new LocalMailbox((LocalMailboxURL) url);
            }
            mb.setLimitFilter(filter);
            mb.setLimitPattern(limitPattern);
            editor.makeNext(mb);
            editor.switchToBuffer(mb);
        }
    }
