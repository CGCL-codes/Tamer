    void parse(XMLElement element, ClientConnection issuer) {
        if (issuer.hasOffer()) {
            issuer.getOffer().replyToOfferor(element.toString());
            issuer.setOffer(null);
        }
        if (issuer.getFlagger() != null) {
            if (element.getName().equals("clkcheck")) {
                issuer.getFlagger().send(element);
                issuer.setFlagger(null);
            }
        }
        if (issuer.isInGameState() && element.getName().equals("move")) {
            issuer.doMove(element);
        } else if (element.getName().equals("request")) {
            if (element.getAttribute("type").equals("MatchRequest")) {
                ClientConnection cc = getConnectionOf(element.getStringAttribute("username"));
                try {
                    if (cc.isInGameState()) {
                        issuer.send(Message.Reply.toXMLElement(false, cc.getUser().getUsername() + " is already playing a game."));
                        return;
                    } else if (cc == issuer) {
                        issuer.send(Message.Reply.toXMLElement(false, "You cannot match yourself."));
                        return;
                    }
                    cc.setOffer(new Offer(issuer, cc, element, Offer.Type.MatchOffer));
                    element.setAttribute("username", issuer.getUser().getUsername());
                    cc.send(element);
                    issuer.send(Message.Info.toXMLElement("Match request sent"));
                } catch (NullPointerException ex) {
                    issuer.send(Message.Reply.toXMLElement(false, element.getStringAttribute("username") + " isn't logged in!"));
                }
            } else if (issuer.isInGameState() && isGameRelatedRequest((String) element.getAttribute("type"))) {
                parseGameRelatedRequest(element, issuer);
            } else {
                try {
                    issuer.send(execRequest(element, issuer));
                } catch (Exception ex) {
                    issuer.send(Message.Reply.toXMLElement(false, ex.getMessage()));
                    ex.printStackTrace();
                }
            }
        } else if (element.getName().equals("privtell")) {
            String receiver = element.getStringAttribute("username");
            try {
                ClientConnection cc = getConnectionOf(receiver);
                element.setAttribute("username", issuer.getUser().getUsername());
                cc.send(element);
                issuer.send(Message.Reply.toXMLElement(true, "Sent to " + receiver));
            } catch (NullPointerException e) {
                issuer.send(Message.Reply.toXMLElement(false, receiver + " isn't logged in."));
            }
        } else if (element.getName().equals("chantell")) {
            Channel cn = JinyanServer.getInstance().getChannel(element.getIntAttribute("channnumber"));
            element.setAttribute("username", issuer.getUser().getUsername());
            cn.send(element);
        } else if (element.getName().equals("whisper")) {
            OnlineChessGame game = null;
            int n = element.getIntAttribute("gamenumber");
            if (n == 0) {
                if (issuer.isInGameState()) {
                    game = issuer.getCurrentGame();
                } else game = issuer.primaryGame;
            } else if (issuer.isObservingGame(n)) game = issuer.observingGames.get(n);
            if (game != null) {
                element.setAttribute("username", issuer.getUser().getUsername());
                game.whisper(element);
            } else {
                issuer.tellDenied();
            }
        } else if (element.getName().equals("kibitz")) {
            OnlineChessGame game = null;
            int n = element.getIntAttribute("channnumber");
            if (n == 0) {
                if (issuer.isInGameState()) {
                    game = issuer.getCurrentGame();
                } else game = issuer.primaryGame;
            } else if (issuer.isObservingGame(n)) game = issuer.observingGames.get(n);
            if (game != null) {
                element.setAttribute("username", issuer.getUser().getUsername());
                game.kibitz(element);
            } else {
                issuer.tellDenied();
            }
        }
    }
