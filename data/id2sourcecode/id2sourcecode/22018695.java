    private XMLElement execRequest(XMLElement element, ClientConnection issuer) {
        if (element.getAttribute("type").equals("NotesModif")) {
            issuer.getUser().setNotes(element.getContent());
            JinyanServer.ib.updateUser(issuer.getUser());
            return Message.Reply.toXMLElement(true);
        } else if (element.getAttribute("type").equals("Finger")) {
            ClientConnection cc = JinyanServer.getConnectionOf((String) element.getAttribute("username"));
            if (cc != null) {
                issuer.send(Message.Info.toXMLElement((Utils.generateUserProfile(cc.getUser()))));
            } else {
                ServerUser usr = JinyanServer.ib.getUser((String) element.getAttribute("username"));
                if (usr != null) issuer.send(Message.Info.toXMLElement(Utils.generateUserProfile(usr))); else return Message.Reply.toXMLElement(false, "No such user");
            }
            return Message.Reply.toXMLElement(true);
        } else if (element.getAttribute("type").equals("History")) {
            ClientConnection cc = JinyanServer.getConnectionOf((String) element.getAttribute("username"));
            if (cc != null) {
                issuer.send(Message.Info.toXMLElement((Utils.generateUserHistory(cc.getUser()))));
            } else {
                ServerUser usr = JinyanServer.ib.getUser((String) element.getAttribute("username"));
                if (usr != null) issuer.send(Message.Info.toXMLElement(Utils.generateUserHistory(usr))); else return Message.Reply.toXMLElement(false, "No such user");
            }
            return null;
        } else if (element.getAttribute("type").equals("GameInfo")) {
            if (element.getAttribute("gamenumber").equals("*")) {
                StringBuffer result = new StringBuffer();
                for (OnlineChessGame game : JinyanServer.liveGames) {
                    result.append(game.toXML());
                }
                issuer.send(Message.Info.toXMLElement(result.toString()));
                return null;
            } else {
                int n = element.getIntAttribute("gamenumber");
                OnlineChessGame game = null;
                for (OnlineChessGame gm : JinyanServer.liveGames) {
                    if (gm.getId() == n) game = gm;
                }
                if (game == null) return Message.Reply.toXMLElement(false); else {
                    issuer.send(game.toXML());
                    return null;
                }
            }
        } else if (element.getAttribute("type").equals("GameObserve")) {
            if (element.getAttribute("gamenumber") == null) {
                return Message.Reply.toXMLElement(issuer.gameObserve(element.getStringAttribute("username")));
            }
            return Message.Reply.toXMLElement(issuer.gameObserve(element.getIntAttribute("gamenumber")));
        } else if (element.getAttribute("type").equals("GameUnobserve")) {
            int num = element.getIntAttribute("gamenumber");
            OnlineChessGame gm = JinyanServer.liveGames.get(num - 1);
            if (gm == null || !gm.isObserving(issuer)) return Message.Reply.toXMLElement(false);
            gm.removeObserver(issuer);
            return Message.Reply.toXMLElement(true, "You are not observing game " + num + " now");
        } else if (element.getAttribute("type").equals("ChannAdd")) {
            Channel cn = JinyanServer.getInstance().getChannel(element.getIntAttribute("channnumber"));
            cn.addSubscrb(issuer);
            return Message.Reply.toXMLElement(true, "You are now subscribed to channel " + element.getIntAttribute("channnumber"));
        } else if (element.getAttribute("type").equals("ChannRemove")) {
            Channel cn = JinyanServer.getInstance().getChannel(element.getIntAttribute("channnumber"));
            boolean removed = cn.removeSubscrb(issuer);
            return Message.Reply.toXMLElement(removed, "Channel " + element.getIntAttribute("channnumber") + " was removed from your list");
        } else if (element.getAttribute("type").equals("Resume")) {
            if (JinyanServer.ib.hasAdjournedGames(issuer.getUser().getUsername())) {
                return Message.Reply.toXMLElement(JinyanServer.ib.attemptToResumeGame(issuer.getUser().getUsername(), (String) element.getAttribute("opponent")));
            }
            return Message.Reply.toXMLElement(false);
        } else if (element.getAttribute("type").equals("AdjournedList")) {
            XMLElement list = JinyanServer.ib.getAdjournedList(issuer.getUser().getUsername());
            issuer.send(list);
            return null;
        } else return Message.Reply.toXMLElement(false);
    }
