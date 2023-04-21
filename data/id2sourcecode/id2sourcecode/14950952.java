    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int petId = slea.readInt();
        int petIndex = c.getPlayer().getPetIndex(petId);
        MaplePet pet = null;
        if (petIndex == -1) {
            return;
        } else {
            pet = c.getPlayer().getPet(petIndex);
        }
        slea.readInt();
        slea.readByte();
        byte command = slea.readByte();
        PetCommand petCommand = PetDataFactory.getPetCommand(pet.getItemId(), (int) command);
        boolean success = false;
        Random rand = new Random();
        int random = rand.nextInt(101);
        if (random <= petCommand.getProbability()) {
            success = true;
            if (pet.getCloseness() < 30000) {
                int newCloseness = pet.getCloseness() + (petCommand.getIncrease() * c.getChannelServer().getPetExpRate());
                if (newCloseness > 30000) {
                    newCloseness = 30000;
                }
                pet.setCloseness(newCloseness);
                if (newCloseness >= ExpTable.getClosenessNeededForLevel(pet.getLevel() + 1)) {
                    pet.setLevel(pet.getLevel() + 1);
                    c.getSession().write(MaplePacketCreator.showOwnPetLevelUp(c.getPlayer().getPetIndex(pet)));
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.showPetLevelUp(c.getPlayer(), c.getPlayer().getPetIndex(pet)));
                }
                c.getSession().write(MaplePacketCreator.updatePet(pet, true));
            }
        }
        MapleCharacter player = c.getPlayer();
        player.getMap().broadcastMessage(player, MaplePacketCreator.commandResponse(player.getId(), command, petIndex, success, false), true);
    }
