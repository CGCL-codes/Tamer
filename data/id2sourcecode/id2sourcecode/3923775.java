    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        int slot1 = m.getIntParameter(0, 0);
        int slot2 = m.getIntParameter(1, 0);
        if (slot1 >= 1 && slot1 <= 6 && slot2 >= 1 && slot2 <= 6 && slot1 != slot2) {
            PlayerSwitchMessage pswitch = new PlayerSwitchMessage();
            pswitch.setSlot1(slot1);
            pswitch.setSlot2(slot2);
            client.getChannel().send(pswitch);
        }
    }
