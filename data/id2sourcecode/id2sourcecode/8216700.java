        public void updateDisplay() {
            ChatChannel channel = getChannel();
            channel.addObserver(this);
            if (channel.isMember(getUser())) {
                if (!wasMember) {
                    buffer.insert(0, getLocalizer().getString("inChannel", page) + " " + channel.getName() + ".\n");
                    wasMember = true;
                }
                List newMsgs = channel.getNewMessages(messageCount);
                Iterator i = newMsgs.iterator();
                while (i.hasNext()) {
                    ChatMessage msg = (ChatMessage) i.next();
                    String style = msg.getMsg().contains("has left the Lobby") ? "chatLEFT" : "chatJOIN";
                    String insert;
                    if (msg.getNick() == null) {
                        insert = "<span class=\"" + style + "\">" + msg + "</span>";
                    } else if (msg.getNick().equals(getUser().getName())) {
                        insert = "<span class=\"chatTEXT\">" + msg.toString().replaceAll("<", "&lt;") + "</span>";
                    } else {
                        insert = msg.toString().replaceAll("<", "&lt;");
                    }
                    buffer.insert(0, insert + "\n");
                }
                messageCount += newMsgs.size();
            } else {
                if (wasMember) {
                    buffer.append(getLocalizer().getString("notInChannel", page) + " " + channel.getName() + ".\n");
                    wasMember = false;
                }
            }
            properties.put("chatDisplay", buffer.toString());
        }
