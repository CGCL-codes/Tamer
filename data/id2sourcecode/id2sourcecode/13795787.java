    public void take(BagItem bitem, UserMessage m) {
        if (bag.isFull()) {
            BagItem drop = bag.getRandomItem();
            bag.removeItem(drop);
            String person = m.getChannel().getRandomUser().getNick();
            m.reply(templates.applyExchange(drop, bitem, person), false);
        } else {
            m.reply(templates.applyPut(bitem), false);
        }
        bag.addItem(bitem);
        manager.saveBag();
    }
