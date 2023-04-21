    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        byte mode = slea.readByte();
        if (mode == Action.CREATE.getCode()) {
            byte createType = slea.readByte();
            if (createType == 3) MapleTrade.startTrade(c.getPlayer()); else if (createType == 1) {
                if (c.getPlayer().getChalkboard() != null) return;
                String desc = slea.readMapleAsciiString();
                slea.readByte();
                int type = slea.readByte();
                MapleMiniGame game = new MapleMiniGame(c.getPlayer(), desc);
                c.getPlayer().setMiniGame(game);
                game.setPieceType(type);
                game.setGameType("omok");
                c.getPlayer().getMap().addMapObject(game);
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.addOmokBox(c.getPlayer(), 1, 0));
                game.sendOmok(c, type);
            } else if (createType == 2) {
                if (c.getPlayer().getChalkboard() != null) return;
                String desc = slea.readMapleAsciiString();
                slea.readByte();
                int type = slea.readByte();
                MapleMiniGame game = new MapleMiniGame(c.getPlayer(), desc);
                game.setPieceType(type);
                if (type == 0) game.setMatchesToWin(6);
                if (type == 1) game.setMatchesToWin(10);
                if (type == 2) game.setMatchesToWin(15);
                game.setGameType("matchcard");
                c.getPlayer().setMiniGame(game);
                c.getPlayer().getMap().addMapObject(game);
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.addMatchCardBox(c.getPlayer(), 1, 0));
                game.sendMatchCard(c, type);
            } else if (createType == 4) {
                if (c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 19500, Arrays.asList(MapleMapObjectType.SHOP, MapleMapObjectType.HIRED_MERCHANT)).size() != 0) {
                    c.getPlayer().dropMessage(1, "You may not establish a store here.");
                    return;
                }
                String desc = slea.readMapleAsciiString();
                slea.readByte();
                slea.readShort();
                slea.readInt();
                MaplePlayerShop shop = new MaplePlayerShop(c.getPlayer(), desc);
                c.getPlayer().setPlayerShop(shop);
                c.getPlayer().getMap().addMapObject(shop);
                shop.sendShop(c);
                c.getSession().write(MaplePacketCreator.getPlayerShopRemoveVisitor(1));
            } else if (createType == 5) {
                if (c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 19500, Arrays.asList(MapleMapObjectType.SHOP, MapleMapObjectType.HIRED_MERCHANT)).size() != 0) {
                    c.getPlayer().dropMessage(1, "You may not establish a store here.");
                    return;
                }
                String desc = slea.readMapleAsciiString();
                slea.skip(3);
                int itemId = slea.readInt();
                if (c.getPlayer().getInventory(MapleInventoryType.CASH).countById(itemId) <= 0) return;
                if (c.getPlayer().getMapId() > 910000000 && c.getPlayer().getMapId() < 910000023) {
                    HiredMerchant merchant = new HiredMerchant(c.getPlayer(), itemId, desc);
                    c.getPlayer().setHiredMerchant(merchant);
                    c.getSession().write(MaplePacketCreator.getHiredMerchant(c.getPlayer(), merchant, true));
                }
            } else System.out.println("Unhandled PLAYER_INTERACTION packet: " + slea.toString());
        } else if (mode == Action.INVITE.getCode()) {
            int otherPlayer = slea.readInt();
            MapleCharacter otherChar = c.getPlayer().getMap().getCharacterById(otherPlayer);
            MapleTrade.inviteTrade(c.getPlayer(), otherChar);
        } else if (mode == Action.DECLINE.getCode()) MapleTrade.declineTrade(c.getPlayer()); else if (mode == Action.VISIT.getCode()) if (c.getPlayer().getTrade() != null && c.getPlayer().getTrade().getPartner() != null) MapleTrade.visitTrade(c.getPlayer(), c.getPlayer().getTrade().getPartner().getChr()); else {
            int oid = slea.readInt();
            MapleMapObject ob = c.getPlayer().getMap().getMapObject(oid);
            if (ob instanceof MaplePlayerShop) {
                MaplePlayerShop shop = (MaplePlayerShop) ob;
                if (shop.hasFreeSlot() && !shop.isVisitor(c.getPlayer())) {
                    shop.addVisitor(c.getPlayer());
                    c.getPlayer().setPlayerShop(shop);
                    shop.sendShop(c);
                }
            } else if (ob instanceof MapleMiniGame) {
                MapleMiniGame game = (MapleMiniGame) ob;
                if (game.hasFreeSlot() && !game.isVisitor(c.getPlayer())) {
                    if (game.getGameType().equals("omok")) {
                        game.addVisitor(c.getPlayer());
                        c.getPlayer().setMiniGame(game);
                        game.sendOmok(c, game.getPieceType());
                    }
                    if (game.getGameType().equals("matchcard")) {
                        game.addVisitor(c.getPlayer());
                        c.getPlayer().setMiniGame(game);
                        game.sendMatchCard(c, game.getPieceType());
                    }
                } else c.getPlayer().getClient().getSession().write(MaplePacketCreator.getMiniGameFull());
            } else if (ob instanceof HiredMerchant && c.getPlayer().getHiredMerchant() == null) {
                HiredMerchant merchant = (HiredMerchant) ob;
                c.getPlayer().setHiredMerchant(merchant);
                if (merchant.isOwner(c.getPlayer())) {
                    merchant.setOpen(false);
                    merchant.removeAllVisitors("");
                    c.getSession().write(MaplePacketCreator.getHiredMerchant(c.getPlayer(), merchant, false));
                } else if (!merchant.isOpen()) c.getPlayer().dropMessage(1, "This shop is in maintenance, please come by later."); else if (merchant.getFreeSlot() == -1) c.getPlayer().dropMessage(1, "This shop has reached it's maximum capacity, please come by later."); else {
                    merchant.addVisitor(c.getPlayer());
                    c.getSession().write(MaplePacketCreator.getHiredMerchant(c.getPlayer(), merchant, false));
                }
            }
        } else if (mode == Action.CHAT.getCode()) {
            HiredMerchant merchant = c.getPlayer().getHiredMerchant();
            if (c.getPlayer().getTrade() != null) c.getPlayer().getTrade().chat(slea.readMapleAsciiString()); else if (c.getPlayer().getPlayerShop() != null) {
                MaplePlayerShop shop = c.getPlayer().getPlayerShop();
                if (shop != null) shop.chat(c, slea.readMapleAsciiString());
            } else if (c.getPlayer().getMiniGame() != null) {
                MapleMiniGame game = c.getPlayer().getMiniGame();
                if (game != null) game.chat(c, slea.readMapleAsciiString());
            } else if (merchant != null) {
                String message = slea.readMapleAsciiString();
                merchant.broadcastToVisitors(MaplePacketCreator.hiredMerchantChat(c.getPlayer().getName() + " : " + message, merchant.getVisitorSlot(c.getPlayer()) + 1));
            }
        } else if (mode == Action.EXIT.getCode()) if (c.getPlayer().getTrade() != null) MapleTrade.cancelTrade(c.getPlayer()); else {
            MaplePlayerShop shop = c.getPlayer().getPlayerShop();
            MapleMiniGame game = c.getPlayer().getMiniGame();
            HiredMerchant merchant = c.getPlayer().getHiredMerchant();
            if (shop != null) {
                c.getPlayer().setPlayerShop(null);
                if (shop.isOwner(c.getPlayer())) {
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.removeCharBox(c.getPlayer()));
                    shop.removeVisitors();
                    for (MaplePlayerShopItem item : shop.getItems()) {
                        IItem iItem = item.getItem().copy();
                        iItem.setQuantity((short) (item.getBundles() * iItem.getQuantity()));
                        MapleInventoryManipulator.addFromDrop(c, iItem, "Removing from closing store", false);
                    }
                } else shop.removeVisitor(c.getPlayer());
            } else if (game != null) {
                c.getPlayer().setMiniGame(null);
                if (game.isOwner(c.getPlayer())) {
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.removeCharBox(c.getPlayer()));
                    game.broadcastToVisitor(MaplePacketCreator.getMiniGameClose((byte) 0));
                } else game.removeVisitor(c.getPlayer());
            } else if (merchant != null) {
                if (!merchant.isOwner(c.getPlayer())) merchant.removeVisitor(c.getPlayer()); else c.getSession().write(MaplePacketCreator.hiredMerchantVisitorLeave(0, true));
                c.getPlayer().setHiredMerchant(null);
            }
        } else if (mode == Action.OPEN.getCode()) {
            MaplePlayerShop shop = c.getPlayer().getPlayerShop();
            HiredMerchant merchant = c.getPlayer().getHiredMerchant();
            if (shop != null && shop.isOwner(c.getPlayer())) {
                slea.readByte();
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.addCharBox(c.getPlayer(), 4));
            } else if (merchant != null && merchant.isOwner(c.getPlayer())) {
                c.getPlayer().setHasMerchant(true);
                merchant.setOpen(true);
                c.getPlayer().getMap().addMapObject(merchant);
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.spawnHiredMerchant(merchant));
                slea.readByte();
            }
        } else if (mode == Action.READY.getCode()) {
            MapleMiniGame game = c.getPlayer().getMiniGame();
            game.broadcast(MaplePacketCreator.getMiniGameReady(game));
        } else if (mode == Action.UN_READY.getCode()) {
            MapleMiniGame game = c.getPlayer().getMiniGame();
            game.broadcast(MaplePacketCreator.getMiniGameUnReady(game));
        } else if (mode == Action.START.getCode()) {
            MapleMiniGame game = c.getPlayer().getMiniGame();
            if (game.getGameType().equals("omok")) {
                game.broadcast(MaplePacketCreator.getMiniGameStart(game, game.getLoser()));
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.addOmokBox(game.getOwner(), 2, 1));
                game.setStarted(1);
            }
            if (game.getGameType().equals("matchcard")) {
                game.shuffleList();
                game.broadcast(MaplePacketCreator.getMatchCardStart(game, game.getLoser()));
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.addMatchCardBox(game.getOwner(), 2, 1));
                game.setStarted(1);
            }
        } else if (mode == Action.GIVE_UP.getCode()) {
            MapleMiniGame game = c.getPlayer().getMiniGame();
            if (game.getGameType().equals("omok")) if (game.isOwner(c.getPlayer())) game.broadcast(MaplePacketCreator.getMiniGameOwnerForfeit(game)); else game.broadcast(MaplePacketCreator.getMiniGameVisitorForfeit(game));
            if (game.getGameType().equals("matchcard")) if (game.isOwner(c.getPlayer())) game.broadcast(MaplePacketCreator.getMatchCardVisitorWin(game)); else game.broadcast(MaplePacketCreator.getMatchCardOwnerWin(game));
        } else if (mode == Action.REQUEST_TIE.getCode()) {
            MapleMiniGame game = c.getPlayer().getMiniGame();
            if (game.isOwner(c.getPlayer())) game.broadcastToVisitor(MaplePacketCreator.getMiniGameRequestTie(game)); else game.getOwner().getClient().getSession().write(MaplePacketCreator.getMiniGameRequestTie(game));
        } else if (mode == Action.ANSWER_TIE.getCode()) {
            MapleMiniGame game = c.getPlayer().getMiniGame();
            int type = slea.readByte();
            if (game.getGameType().equals("omok")) game.broadcast(MaplePacketCreator.getMiniGameTie(game));
            if (game.getGameType().equals("matchcard")) game.broadcast(MaplePacketCreator.getMatchCardTie(game));
        } else if (mode == Action.SKIP.getCode()) {
            MapleMiniGame game = c.getPlayer().getMiniGame();
            if (game.isOwner(c.getPlayer())) game.broadcast(MaplePacketCreator.getMiniGameSkipOwner(game)); else game.broadcast(MaplePacketCreator.getMiniGameSkipVisitor(game));
        } else if (mode == Action.MOVE_OMOK.getCode()) {
            int x = slea.readInt();
            int y = slea.readInt();
            int type = slea.readByte();
            c.getPlayer().getMiniGame().setPiece(x, y, type, c.getPlayer());
        } else if (mode == Action.SELECT_CARD.getCode()) {
            int turn = slea.readByte();
            int slot = slea.readByte();
            MapleMiniGame game = c.getPlayer().getMiniGame();
            int firstslot = game.getFirstSlot();
            if (turn == 1) {
                game.setFirstSlot(slot);
                if (game.isOwner(c.getPlayer())) game.broadcastToVisitor(MaplePacketCreator.getMatchCardSelect(game, turn, slot, firstslot, turn)); else game.getOwner().getClient().getSession().write(MaplePacketCreator.getMatchCardSelect(game, turn, slot, firstslot, turn));
            } else if ((game.getCardId(firstslot + 1)) == (game.getCardId(slot + 1))) if (game.isOwner(c.getPlayer())) {
                game.broadcast(MaplePacketCreator.getMatchCardSelect(game, turn, slot, firstslot, 2));
                game.setOwnerPoints();
            } else {
                game.broadcast(MaplePacketCreator.getMatchCardSelect(game, turn, slot, firstslot, 3));
                game.setVisitorPoints();
            } else if (game.isOwner(c.getPlayer())) game.broadcast(MaplePacketCreator.getMatchCardSelect(game, turn, slot, firstslot, 0)); else game.broadcast(MaplePacketCreator.getMatchCardSelect(game, turn, slot, firstslot, 1));
        } else if (mode == Action.SET_MESO.getCode()) c.getPlayer().getTrade().setMeso(slea.readInt()); else if (mode == Action.SET_ITEMS.getCode()) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            MapleInventoryType ivType = MapleInventoryType.getByType(slea.readByte());
            IItem item = c.getPlayer().getInventory(ivType).getItem((byte) slea.readShort());
            short quantity = slea.readShort();
            byte targetSlot = slea.readByte();
            if (c.getPlayer().getTrade() != null) if ((quantity <= item.getQuantity() && quantity >= 0) || ii.isThrowingStar(item.getItemId()) || ii.isBullet(item.getItemId())) {
                if (!c.getChannelServer().allowUndroppablesDrop() && ii.isDropRestricted(item.getItemId())) {
                    c.getSession().write(MaplePacketCreator.enableActions());
                    return;
                }
                IItem tradeItem = item.copy();
                if (ii.isThrowingStar(item.getItemId()) || ii.isBullet(item.getItemId())) {
                    tradeItem.setQuantity(item.getQuantity());
                    MapleInventoryManipulator.removeFromSlot(c, ivType, item.getPosition(), item.getQuantity(), true);
                } else {
                    tradeItem.setQuantity(quantity);
                    MapleInventoryManipulator.removeFromSlot(c, ivType, item.getPosition(), quantity, true);
                }
                tradeItem.setPosition(targetSlot);
                c.getPlayer().getTrade().addItem(tradeItem);
                return;
            }
        } else if (mode == Action.CONFIRM.getCode()) MapleTrade.completeTrade(c.getPlayer()); else if (mode == Action.ADD_ITEM.getCode() || mode == Action.PUT_ITEM.getCode()) {
            MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());
            byte slot = (byte) slea.readShort();
            short bundles = slea.readShort();
            short perBundle = slea.readShort();
            int price = slea.readInt();
            if (perBundle > 2000 || perBundle < 0 || bundles > 2000 || bundles < 0 || price > 2147483647 || price < 0) return;
            IItem ivItem = c.getPlayer().getInventory(type).getItem(slot);
            IItem sellItem = ivItem.copy();
            sellItem.setQuantity(perBundle);
            MaplePlayerShopItem item = new MaplePlayerShopItem(sellItem, bundles, price);
            MaplePlayerShop shop = c.getPlayer().getPlayerShop();
            HiredMerchant merchant = c.getPlayer().getHiredMerchant();
            try {
                if (shop != null && shop.isOwner(c.getPlayer())) {
                    if (ivItem != null && ivItem.getQuantity() >= bundles * perBundle) {
                        shop.addItem(item);
                        c.getSession().write(MaplePacketCreator.getPlayerShopItemUpdate(shop));
                    }
                } else if (merchant != null && merchant.isOwner(c.getPlayer())) {
                    merchant.addItem(item);
                    c.getSession().write(MaplePacketCreator.hiredMerchantUpdate(merchant));
                }
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                if (ii.isThrowingStar(ivItem.getItemId()) || ii.isBullet(ivItem.getItemId())) MapleInventoryManipulator.removeFromSlot(c, type, slot, ivItem.getQuantity(), true); else MapleInventoryManipulator.removeFromSlot(c, type, slot, (short) (bundles * perBundle), true);
            } catch (Exception e) {
                c.getPlayer().dropMessage("Exception:" + e);
            }
        } else if (mode == Action.REMOVE_ITEM.getCode()) {
            MaplePlayerShop shop = c.getPlayer().getPlayerShop();
            if (shop != null && shop.isOwner(c.getPlayer())) {
                int slot = slea.readShort();
                MaplePlayerShopItem item = shop.getItems().get(slot);
                IItem ivItem = item.getItem().copy();
                shop.removeItem(slot);
                ivItem.setQuantity((short) (item.getBundles() * ivItem.getQuantity()));
                MapleInventoryManipulator.addFromDrop(c, ivItem, "Removing item from store", false);
                c.getSession().write(MaplePacketCreator.getPlayerShopItemUpdate(shop));
            }
        } else if (mode == Action.BUY.getCode() || mode == Action.MERCHANT_BUY.getCode()) {
            int item = slea.readByte();
            short quantity = slea.readShort();
            MaplePlayerShop shop = c.getPlayer().getPlayerShop();
            HiredMerchant merchant = c.getPlayer().getHiredMerchant();
            if (merchant.getOwner().equals(c.getPlayer().getName())) return;
            if (shop != null && shop.isVisitor(c.getPlayer())) {
                shop.buy(c, item, quantity);
                StringBuilder logInfo = new StringBuilder("Taken out from player shop by ");
                logInfo.append(c.getPlayer().getName());
                shop.broadcast(MaplePacketCreator.getPlayerShopItemUpdate(shop));
            } else if (merchant != null) {
                merchant.buy(c, item, quantity);
                merchant.broadcastToVisitors(MaplePacketCreator.hiredMerchantUpdate(merchant));
            }
        } else if (mode == Action.TAKE_ITEM_BACK.getCode()) {
            int slot = slea.readShort();
            HiredMerchant merchant = c.getPlayer().getHiredMerchant();
            if (merchant != null) {
                MaplePlayerShopItem item = merchant.getItems().get(slot);
                if (item.getBundles() > 0) {
                    IItem iitem = item.getItem();
                    iitem.setQuantity(item.getBundles());
                    MapleInventoryManipulator.addFromDrop(c, iitem, "Taking item back", true);
                }
                merchant.removeFromSlot(slot);
                c.getSession().write(MaplePacketCreator.hiredMerchantUpdate(merchant));
            }
        } else if (mode == Action.CLOSE_MERCHANT.getCode()) {
            HiredMerchant merchant = c.getPlayer().getHiredMerchant();
            MapleCharacter player = c.getPlayer();
            try {
                if (merchant != null && merchant.isOwner(c.getPlayer())) {
                    for (MaplePlayerShopItem mpsi : merchant.getItems()) {
                        IItem item = mpsi.getItem();
                        if (item.getQuantity() > 0) {
                            if (MapleInventoryManipulator.addFromDrop(c, item, "Taking item from merchant from closing", true)) {
                                merchant.removeFromSlot(merchant.getItems().indexOf(mpsi));
                                item.setQuantity((short) 0);
                            } else {
                                player.dropMessage("Item couldn't be added o_O...");
                            }
                        } else {
                            player.dropMessage("Not > then 0 o_O...");
                        }
                    }
                    c.getSession().write(MaplePacketCreator.hiredMerchantForceLeave2());
                    merchant.closeShop();
                    c.getPlayer().setHasMerchant(false);
                }
            } catch (Exception e) {
                player.dropMessage("Exception: " + e);
            }
        } else if (mode == Action.MAINTENANCE_OFF.getCode()) {
            HiredMerchant merchant = c.getPlayer().getHiredMerchant();
            if (merchant != null && merchant.isOwner(c.getPlayer())) merchant.setOpen(true);
        }
    }
