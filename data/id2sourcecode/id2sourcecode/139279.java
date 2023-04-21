                public void iButtonInserted(JibMultiEvent event) {
                    playSound("inserted.au");
                    int slot = event.getSlotID();
                    SlotChannel channel = event.getChannel();
                    iButtonCardTerminal terminal = (iButtonCardTerminal) channel.getCardTerminal();
                    int[] buttonId = terminal.getiButtonId(channel.getSlotNumber());
                    AnimalProxy re_ap = AnimalRegistry.getReusableAnimalProxy(buttonId);
                    if (re_ap == null) {
                        boolean selected = AnimalProxy.selectApplet(channel);
                        if (selected) {
                            LocationPanel lp = getSelectedLocationPanel();
                            AnimalProxy new_ap;
                            if (lp != null) new_ap = lp.getFactory().createAnimalProxy(channel, buttonId); else new_ap = new AnimalProxy(channel, buttonId);
                            addAnimal(new_ap);
                        } else {
                            System.err.println("iButton applet \"Animal\" not found");
                            playSound("failure.au");
                        }
                    } else {
                        re_ap.setChannel(channel);
                    }
                }
