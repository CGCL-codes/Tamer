    void parse(Element e) {
        String tagname = e.name;
        if (tagname.equals("privatejoin")) {
            String roomid = e.getAttr("roomid");
            String password = "";
            String who = e.getElement("username").getText();
            this.a.csm.getSession(roomid).addConnection(this.a.csm.getSession("mainhall").getUser(who));
            if (e.getElement("password") != null) {
                password = e.getElement("password").getText();
            }
            if (a.csm.getSession(roomid) != null) {
                if (a.csm.getSession(roomid).password.equals(password)) {
                    a.csm.getSession(roomid).addConnection(this);
                    this.roomid = roomid;
                } else {
                }
            }
            this.roomid = roomid;
        }
        if (tagname.equals("join")) {
            String roomid = e.getElement("roomid").getText();
            String password = "";
            if (e.getElement("password") != null) {
                password = e.getElement("password").getText();
            }
            if (a.csm.getSession(roomid) != null) {
                if (a.csm.getSession(roomid).password.equals(password)) {
                    if (a.csm.getSession(roomid).priv) {
                        a.csm.getSession(roomid).askOwnerForPermissionToJoin(this.name);
                    } else {
                        a.csm.getSession(roomid).addConnection(this);
                        this.roomid = roomid;
                    }
                } else {
                }
            } else {
                this.owner = true;
                a.csm.createSession(roomid, password);
                a.csm.getSession(roomid).addConnection(this);
                Element priv = e.getElement("private");
                if (priv != null) {
                    a.csm.getSession(roomid).setPrivate(true);
                }
                System.out.println("Setting owner.");
                this.roomid = roomid;
            }
        } else if (tagname.equals("leave")) {
            ChatSession cs = this.a.csm.getSession(e.getElement("roomid").getText());
            cs.removeConnection(this);
        } else if (tagname.equals("message")) {
            String type = e.getAttr("type");
            String to = e.getAttr("roomid");
            if (type.equals("conference")) {
                if (a.csm.getSession(to) != null) {
                    e.attributes.put("from", this.name);
                    a.csm.getSession(to).dispatch(e.toString());
                }
                Element e2 = e.getElement("owner");
                if (e2 != null) {
                    Element e21 = e2.getElement("code");
                    Element e3 = e2.getElement("username");
                    if (e21 != null & e3 != null) {
                        a.csm.getSession(to).changeUserRights(this, e3.getText(), e21.getText());
                    }
                }
            } else {
                e.addAttr("from", this.name);
                System.out.println("Private message recieved. ");
                Vector v = this.a.csm.sessions;
                for (int i = 0; i < v.size(); i++) {
                    ChatSession cs = (ChatSession) v.elementAt(i);
                    Vector v2 = cs.cons;
                    for (int j = 0; j < v2.size(); j++) {
                        ChatConnection c = (ChatConnection) v2.elementAt(j);
                        if (c.name.equals(e.getAttr("to"))) {
                            c.send(e.toString());
                            System.out.println("Private message sent.");
                            return;
                        }
                    }
                }
            }
        } else if (tagname.equals("administration")) {
            if (e.getElement("newuser") != null) {
                Element e1 = e.getElement("newuser");
                String username = e1.getElement("username").getText();
                String password = e1.getElement("password").getText();
                if (a.dblayer.userExists(username)) {
                } else {
                    a.dblayer.createUser(username, password);
                }
            }
        } else if (tagname.equals("get")) {
            System.out.println("Parsing get.");
            Element e1 = e.getElement("conferencelist");
            if (e1 != null) {
                this.send(this.a.csm.getChannelList());
                System.out.println("Transmitting room list.");
            }
            e1 = e.getElement("filelist");
            if (e1 != null) {
                this.pushFolderContent();
            }
        } else if (tagname.equals("whiteboard") & maydraw) {
            String to = e.getAttr("roomid");
            if (a.csm.getSession(to) != null) {
                e.attributes.put("from", this.name);
                a.csm.getSession(to).dispatchExcept(e.toString(), this);
            }
        } else if (tagname.equals("fileput")) {
            fn = e.getElement("name").getText();
            data = e.getElement("content").getText();
            a.dblayer.saveFile(fn, data);
            this.pushFolderContent();
        } else if (tagname.equals("kick")) {
            String username = e.getText();
            ChatSession cs = a.csm.getSession(this.roomid);
            if (cs != null) cs.kickUser(username); else System.out.println("chat session not found.");
        } else if (tagname.equals("title")) {
            ChatSession cs = a.csm.getSession(this.roomid);
            if (cs != null) cs.setTitle(e.getText()); else System.out.println("chat session not found.");
        } else if (tagname.equals("mute")) {
            ChatSession cs = a.csm.getSession(this.roomid);
            if (cs != null) {
                this.a.vsm.mute(e.getText());
            }
        } else if (tagname.equals("demute")) {
            ChatSession cs = a.csm.getSession(this.roomid);
            if (cs != null) {
                this.a.vsm.demute(e.getText());
            }
        }
    }
