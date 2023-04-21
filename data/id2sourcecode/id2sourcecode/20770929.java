    public void run() {
        Log.debug("Fetching SVN revisions");
        Session session = botService.getSession();
        if (session == null) {
            Log.warn("No IRC session");
        }
        final List<Channel> channels = session.getChannels();
        if (channels.isEmpty()) {
            Log.warn("No channel in the IRC session");
            return;
        }
        if (StringUtils.isEmpty(svnURL)) {
            Log.warn("No repository provided");
            return;
        }
        SVNRepository repo = null;
        try {
            Log.debug("Creating repository");
            repo = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(svnURL));
            Log.debug("Login in");
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
            repo.setAuthenticationManager(authManager);
            long currentRev = repo.getLatestRevision();
            Log.info("Revision:" + currentRev);
            if (lastRev == -1) {
                lastRev = currentRev;
            } else {
                if (lastRev >= currentRev) {
                    return;
                } else {
                    lastRev = currentRev;
                }
            }
            Log.debug("Fetching entries");
            @SuppressWarnings("unchecked") ArrayList<SVNLogEntry> entries = new ArrayList<SVNLogEntry>(repo.log(new String[] { "" }, null, currentRev, currentRev, true, true));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < entries.size(); ++i) {
                SVNLogEntry current = entries.get(i);
                String msg = current.getMessage();
                if (msg.length() > MAX_MESSAGE_LEN) {
                    msg = new String(msg.substring(0, MAX_MESSAGE_LEN) + " ...");
                }
                sb.append(current.getDate().toString()).append(", ");
                sb.append("Author:").append(current.getAuthor()).append(", r").append(current.getRevision()).append(" ").append(repo.getLocation().getPath()).append(" : ").append(msg).append(", Changed path(s):").append(current.getChangedPaths().size());
            }
            if (!entries.isEmpty()) {
                for (Channel channel : channels) {
                    channel.say(sb.toString());
                }
            }
        } catch (Exception e) {
            for (Channel channel : channels) {
                channel.say(e.getMessage());
            }
        }
    }
