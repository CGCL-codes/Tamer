    public void onMessageReceived(Message message) {
        if (!(message instanceof UserMessage)) {
            return;
        }
        UserMessage m = (UserMessage) message;
        if (!m.getContext().isPublic()) {
            return;
        }
        String text = m.getText();
        String nick = server.getNick();
        String addr = server.getNick() + "[,\\:\\- ] ";
        List<String> l = regex(text, action("gives " + nick + " (\\S+ )?(.+)"));
        if (l != null) {
            String spec = l.get(1);
            if (spec == null) {
                spec = "";
            } else {
                spec = spec.trim();
            }
            String name = l.get(2).trim();
            BagItem bitem = new BagItem(name, spec);
            take(bitem, m);
            return;
        }
        l = regex(text, action("throws (\\S+ )?(.+) at " + nick));
        if (l != null) {
            String spec = l.get(1);
            if (spec == null) {
                spec = "";
            } else {
                spec = spec.trim();
            }
            String name = l.get(2).trim();
            BagItem bitem = new BagItem(name, spec);
            take(bitem, m);
            return;
        }
        l = regex(text, addr + "have (\\S+ )?(.+)");
        if (l != null) {
            String spec = l.get(1);
            if (spec == null) {
                spec = "";
            } else {
                spec = spec.trim();
            }
            String name = l.get(2).trim();
            BagItem bitem = new BagItem(name, spec);
            take(bitem, m);
            return;
        }
        l = regex(text, addr + "throw something");
        if (l != null) {
            if (bag.isEmpty()) {
                m.reply("Sorry " + m.getUser().getNick() + ", my bag is empty", false);
                return;
            }
            BagItem bitem = bag.getRandomItem();
            User target = m.getChannel().getRandomUser();
            m.reply(templates.applyThrow(bitem, target.getNick()), false);
            bag.removeItem(bitem);
            manager.saveBag();
            return;
        }
        l = regex(text, addr + "regift something");
        if (l != null) {
            if (bag.isEmpty()) {
                m.reply("Sorry " + m.getUser().getNick() + ", my bag is empty", false);
                return;
            }
            BagItem bitem = bag.getRandomItem();
            User target = m.getChannel().getRandomUser();
            m.reply(action("gives " + target.getNick() + " " + bitem.getStatement()), false);
            bag.removeItem(bitem);
            manager.saveBag();
            return;
        }
    }
