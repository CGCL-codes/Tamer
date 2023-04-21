    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if ((event.getBlock().getState() instanceof Sign)) {
            Sign signObject = (Sign) event.getBlock().getState();
            if (plugin.WireBox.isReceiver(signObject.getLine(0))) {
                if (plugin.WireBox.hasAccessToChannel(event.getPlayer(), signObject.getLine(1))) {
                    if (plugin.WireBox.RemoveWirelessReceiver(signObject.getLine(1), event.getBlock().getLocation())) {
                        if (plugin.WireBox.getChannel(signObject.getLine(1)).getTransmitters().size() == 0 && plugin.WireBox.getChannel(signObject.getLine(1)).getReceivers().size() == 0) {
                            plugin.WireBox.removeChannel(signObject.getLine(1));
                            event.getPlayer().sendMessage("[WirelessRedstone] Succesfully removed this sign! Channel removed, no more signs in the worlds.");
                        } else {
                            event.getPlayer().sendMessage("[WirelessRedstone] Succesfully removed this sign!");
                        }
                    } else {
                        event.getPlayer().sendMessage("[WirelessRedstone] Something went wrong!");
                    }
                } else {
                    event.getPlayer().sendMessage("[WirelessRedstone] You are not allowed to remove this sign!");
                    event.setCancelled(true);
                }
                return;
            } else if (plugin.WireBox.isTransmitter(signObject.getLine(0))) {
                if (plugin.WireBox.hasAccessToChannel(event.getPlayer(), signObject.getLine(1))) {
                    if (plugin.WireBox.RemoveWirelessTransmitter(signObject.getLine(1), event.getBlock().getLocation())) {
                        event.getPlayer().sendMessage("[WirelessRedstone] Succesfully removed this sign!");
                        if (plugin.WireBox.getChannel(signObject.getLine(1)).getTransmitters().size() == 0) {
                            event.getPlayer().sendMessage("[WirelessRedstone] No other Transmitters found, Resettings Power data on receivers to sign.");
                            for (WirelessReceiver receiver : plugin.WireBox.getChannel(signObject.getLine(1)).getReceivers()) {
                                Location rloc = plugin.WireBox.getPointLocation(receiver);
                                Block othersign = rloc.getBlock();
                                if (receiver.getisWallSign()) {
                                    othersign.getWorld().getBlockAt(rloc).setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) receiver.getDirection(), true);
                                } else {
                                    othersign.getWorld().getBlockAt(rloc).setTypeIdAndData(Material.SIGN_POST.getId(), (byte) receiver.getDirection(), true);
                                }
                                if (othersign.getState() instanceof Sign) {
                                    Sign signtemp = (Sign) othersign.getState();
                                    signtemp.setLine(0, "[WRr]");
                                    signtemp.setLine(1, signObject.getLine(1));
                                    if (receiver.getisWallSign()) {
                                        signtemp.setData(new MaterialData(Material.WALL_SIGN, (byte) receiver.getDirection()));
                                    } else {
                                        signtemp.setData(new MaterialData(Material.SIGN_POST, (byte) receiver.getDirection()));
                                    }
                                    signtemp.update(true);
                                }
                            }
                        }
                    } else {
                        event.getPlayer().sendMessage("[WirelessRedstone] Something went wrong!");
                    }
                } else {
                    event.getPlayer().sendMessage("[WirelessRedstone] You are not allowed to remove this sign!");
                    event.setCancelled(true);
                }
                return;
            }
        } else if (event.getBlock().getType().equals(Material.REDSTONE_TORCH_ON)) {
            for (Location loc : plugin.WireBox.getAllReceiverLocations()) {
                if (loc.equals(event.getBlock().getLocation())) {
                    event.getPlayer().sendMessage("[WirelessRedstone] You cannot break my magic torches my friend!");
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
