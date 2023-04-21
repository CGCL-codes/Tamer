    public void handle(JabberConnection jc) throws IOException {
        String show = getShow();
        Chat gpc = jc.getChannelFor(getFrom());
        if (gpc.getType().equals("chat")) {
            String t = getType();
            if (t.equals("unavailable")) {
                System.err.println("*** " + getFrom() + ": unavailable.");
            } else {
                if (show != null) {
                    System.err.println("*** " + getFrom() + ": " + show);
                } else {
                    System.err.println("*** " + getFrom() + ": available.");
                }
            }
        }
        System.out.println("*#* Presence from " + getFrom());
        if (show == null) {
            Contact c = new Contact(this);
            jc.fireEvent(this, "updateContact", c);
            System.out.println("*#* contact: " + c.getJid());
            String t = getType();
            if (t != null) {
                if (t.equals("unavailable")) {
                    jc.fireEvent(this, "displayInGroupChats", new Object[] { getFrom(), "*** " + getFrom() + " is unavailable." });
                    jc.fireEvent(this, "displayInPrivateChats", new Object[] { getFrom(), "*** " + getFrom() + " is unavailable." });
                    if (jc.getChannelFor(getFrom()) != null) {
                        jc.fireEvent(this, "displayStatus", new Object[] { getFrom(), "unavailable ..." });
                    }
                    jc.fireEvent(this, "removeContact", c);
                } else if (t.equals("subscribe")) {
                    jc.fireEvent(this, "displaySubReq", this);
                }
            } else {
                c.setShow("online");
                jc.fireEvent(this, "updateContact", c);
            }
        } else {
            Contact c = new Contact(this);
            jc.fireEvent(this, "updateContact", c);
            System.out.println("*#* contact: " + c.getJid());
            c.setShow(show);
            System.out.println("show for " + c.getJid() + " set to " + c.getShow());
            c.setStatus("[no status]");
            jc.fireEvent(this, "updateContact", c);
            if (getStatus() != null) {
                c.setStatus(getStatus());
            }
            jc.fireEvent(this, "displayInPrivateChats", new Object[] { getFrom(), "*** " + getFrom() + " is " + c.getShow() + "/" + c.getStatus() + "." });
            if (jc.getChannelFor(getFrom()) != null) {
                jc.fireEvent(this, "displayStatus", getFrom());
            }
        }
        jc.fireEvent(this, "rebuildRoster");
    }
