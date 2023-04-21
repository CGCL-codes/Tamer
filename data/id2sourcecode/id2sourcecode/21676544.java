    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        byte operation = slea.readByte();
        if (operation == Actions.TOSERVER_SEND_ITEM.getCode()) {
            byte inventId = slea.readByte();
            short itemPos = slea.readShort();
            short amount = slea.readShort();
            int mesos = slea.readInt();
            String recipient = slea.readMapleAsciiString();
            int finalcost = mesos + 5000 + getFee(mesos);
            boolean send = false;
            if (c.getPlayer().getMeso() >= finalcost) {
                int accid = getAccIdFromCNAME(recipient);
                if (accid != -1) {
                    if (accid != c.getAccID()) {
                        c.getPlayer().gainMeso(finalcost, false);
                        c.getSession().write(MaplePacketCreator.sendDueyMSG(Actions.TOCLIENT_SUCCESSFULLY_SENT.getCode()));
                        send = true;
                    } else {
                        c.getSession().write(MaplePacketCreator.sendDueyMSG(Actions.TOCLIENT_SAMEACC_ERROR.getCode()));
                    }
                } else {
                    c.getSession().write(MaplePacketCreator.sendDueyMSG(Actions.TOCLIENT_NAME_DOES_NOT_EXIST.getCode()));
                }
            } else {
                c.getSession().write(MaplePacketCreator.sendDueyMSG(Actions.TOCLIENT_NOT_ENOUGH_MESOS.getCode()));
            }
            boolean recipientOn = false;
            MapleClient rClient = null;
            try {
                int channel = c.getChannelServer().getWorldInterface().find(recipient);
                if (channel > -1) {
                    recipientOn = true;
                    ChannelServer rcserv = ChannelServer.getInstance(channel);
                    rClient = rcserv.getPlayerStorage().getCharacterByName(recipient).getClient();
                }
            } catch (RemoteException re) {
                c.getChannelServer().reconnectWorld();
            }
            if (send) {
                if (inventId > 0) {
                    MapleInventoryType inv = MapleInventoryType.getByType(inventId);
                    IItem item = c.getPlayer().getInventory(inv).getItem((byte) itemPos);
                    if (item != null && c.getPlayer().getItemQuantity(item.getItemId(), false) >= amount) {
                        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                        if (ii.isThrowingStar(item.getItemId()) || ii.isBullet(item.getItemId())) {
                            MapleInventoryManipulator.removeFromSlot(c, inv, (byte) itemPos, item.getQuantity(), true);
                        } else {
                            MapleInventoryManipulator.removeFromSlot(c, inv, (byte) itemPos, amount, true, false);
                        }
                        addItemToDB(item, amount, mesos, c.getPlayer().getName(), MapleCharacter.getIdByName(recipient), recipientOn);
                    } else {
                        return;
                    }
                } else {
                    addItemToDB(null, 1, mesos, c.getPlayer().getName(), MapleCharacter.getIdByName(recipient), recipientOn);
                }
                if (recipientOn && rClient != null) {
                    rClient.getSession().write(MaplePacketCreator.sendDueyMSG(Actions.TOCLIENT_PACKAGE_MSG.getCode()));
                }
                c.getPlayer().gainMeso(-5000, false);
            }
        } else if (operation == Actions.TOSERVER_REMOVE_PACKAGE.getCode()) {
            int packageid = slea.readInt();
            removeItemFromDB(packageid);
            c.getSession().write(MaplePacketCreator.removeItemFromDuey(true, packageid));
        } else if (operation == Actions.TOSERVER_CLAIM_PACKAGE.getCode()) {
            int packageid = slea.readInt();
            DueyPackages dp = loadSingleItem(packageid);
            if (dp.getItem() != null) {
                if (!MapleInventoryManipulator.checkSpace(c, dp.getItem().getItemId(), dp.getItem().getQuantity(), dp.getItem().getOwner())) {
                    c.getPlayer().dropMessage(1, "Your inventory is full");
                    c.getSession().write(MaplePacketCreator.enableActions());
                    return;
                } else {
                    MapleInventoryManipulator.addFromDrop(c, dp.getItem(), "Receiving from Duey.", false);
                }
            }
            c.getPlayer().gainMeso(dp.getMesos(), false);
            removeItemFromDB(packageid);
            c.getSession().write(MaplePacketCreator.removeItemFromDuey(false, packageid));
        }
    }
