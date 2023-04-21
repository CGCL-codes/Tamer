    public boolean execute(MOB mob, Vector commands, int metaFlags) throws java.io.IOException {
        PlayerStats pstats = mob.playerStats();
        if (pstats == null) return false;
        int channelInt = CMLib.channels().getChannelIndex("AUCTION");
        int channelNum = CMLib.channels().getChannelCodeNumber("AUCTION");
        if (CMath.isSet(pstats.getChannelMask(), channelInt)) {
            pstats.setChannelMask(pstats.getChannelMask() & (pstats.getChannelMask() - channelNum));
            mob.tell("The AUCTION channel has been turned on.  Use `NOAUCTION` to turn it off again.");
        }
        String cmd = null;
        commands.removeElementAt(0);
        if (commands.size() < 1) cmd = ""; else cmd = ((String) commands.elementAt(0)).toUpperCase();
        if (cmd.equals("LIST")) {
            commands.removeElementAt(0);
            StringBuffer buf = new StringBuffer("");
            if ((liveData.auctioningI != null) && (liveData.auctioningM != null)) {
                buf.append("\n\r^HCurrent *live* auction: ^N\n\r");
                buf.append(liveAuctionStatus() + "\n\r");
            } else buf.append(MESSAGE_NOAUCTION());
            mob.tell(buf.toString());
            return true;
        } else if (cmd.equals("UP")) {
            commands.removeElementAt(0);
            if ((liveData.auctioningI != null) && (liveData.auctioningM != null)) {
                mob.tell("A live auction is already underway.  Do AUCTION LIST to see it.");
                return false;
            }
            Vector V = new Vector();
            if ((commands.size() >= 2) && ((CMLib.english().numPossibleGold(mob, (String) commands.lastElement()) > 0) || (((String) commands.lastElement()).equals("0")))) {
                V.addElement(commands.lastElement());
                commands.removeElementAt(commands.size() - 1);
            } else V.addElement("0");
            String s = CMParms.combine(commands, 0);
            Environmental E = mob.fetchInventory(null, s);
            if ((E == null) || (E instanceof MOB)) {
                mob.tell(s + " is not an item you can auction.");
                return false;
            }
            if ((E instanceof Container) && (((Container) E).getContents().size() > 0)) {
                mob.tell(E.name() + " will have to be emptied first.");
                return false;
            }
            if (!(((Item) E).amWearingAt(Wearable.IN_INVENTORY))) {
                mob.tell(E.name() + " will have to be removed first.");
                return false;
            }
            Auctioneer.AuctionRates aRates = new Auctioneer.AuctionRates();
            double deposit = aRates.liveListPrice;
            String depositAmt = CMLib.beanCounter().nameCurrencyLong(mob, deposit);
            if (deposit > 0.0) {
                if ((mob.isMonster()) || (!mob.session().confirm("Auctioning " + E.name() + " will cost a listing fee of " + depositAmt + ", proceed (Y/n)?", "Y"))) return false;
            } else if ((mob.isMonster()) || (!mob.session().confirm("Auction " + E.name() + " live, with a starting bid of " + ((String) V.firstElement()) + " (Y/n)?", "Y"))) return false;
            if (CMLib.beanCounter().getTotalAbsoluteValue(mob, CMLib.beanCounter().getCurrency(mob)) < deposit) {
                mob.tell("You don't have enough " + CMLib.beanCounter().getDenominationName(CMLib.beanCounter().getCurrency(mob)) + " to cover the listing fee!");
                return false;
            }
            CMLib.beanCounter().subtractMoney(mob, CMLib.beanCounter().getCurrency(mob), deposit);
            doLiveAuction(mob, V, E);
            if (liveData.auctioningI != null) liveData.auctioningM = mob;
            return true;
        } else if (cmd.equals("BID")) {
            commands.removeElementAt(0);
            if ((liveData.auctioningI == null) || (liveData.auctioningM == null)) {
                mob.tell(MESSAGE_NOAUCTION());
                return false;
            }
            if (commands.size() < 1) {
                mob.tell("Bid how much?");
                return false;
            }
            String amount = CMParms.combine(commands, 0);
            doLiveAuction(mob, CMParms.makeVector(amount), null);
            return true;
        } else if (cmd.equals("CLOSE")) {
            commands.removeElementAt(0);
            if ((liveData.auctioningI == null) || (liveData.auctioningM == null)) {
                mob.tell(MESSAGE_NOAUCTION());
                return false;
            }
            if ((liveData.auctioningI == null) || (liveData.auctioningM != mob)) {
                mob.tell("You are not currently running a live auction.");
                return false;
            }
            Vector V = new Vector();
            V.addElement("AUCTION");
            V.addElement("The auction has been closed.");
            CMLib.threads().deleteTick(this, Tickable.TICKID_LIVEAUCTION);
            if (liveData.auctioningI instanceof Item) liveData.auctioningM.giveItem((Item) liveData.auctioningI);
            if (liveData.highBid > 0.0) CMLib.coffeeShops().returnMoney(liveData.highBidderM, liveData.currency, liveData.highBid);
            liveData.auctioningM = null;
            liveData.auctioningI = null;
            super.execute(mob, V, metaFlags);
            return true;
        } else if (cmd.equals("INFO")) {
            commands.removeElementAt(0);
            if ((liveData.auctioningI == null) || (liveData.auctioningM == null)) {
                mob.tell(MESSAGE_NOAUCTION());
                return false;
            }
            Environmental E = null;
            E = liveData.auctioningI;
            mob.tell("Item: " + E.name());
            CMLib.commands().handleBeingLookedAt(CMClass.getMsg(mob, CMMsg.MASK_ALWAYS | CMMsg.MSG_EXAMINE, null));
            mob.tell(CMLib.coffeeShops().getViewDescription(E));
            return true;
        } else if (cmd.equals("CHANNEL")) {
            commands.removeElementAt(0);
            if (commands.size() == 0) {
                mob.tell("Channel what?");
                return false;
            }
            if ((liveData.auctioningI == null) || (liveData.auctioningM == null)) {
                mob.tell("Channeling is only allowed during live auctions.");
                return false;
            }
            commands.insertElementAt("AUCTION", 0);
            super.execute(mob, commands, metaFlags);
            return true;
        }
        commands.insertElementAt("AUCTION", 0);
        super.execute(mob, commands, metaFlags);
        return false;
    }
