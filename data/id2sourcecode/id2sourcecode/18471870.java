    @Override
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
        if (!(event.getBlock().getState() instanceof Sign)) {
            return;
        }
        Sign signObject = (Sign) event.getBlock().getState();
        if (!plugin.WireBox.isTransmitter(signObject.getLine(0)) || signObject.getLine(1) == null || signObject.getLine(1) == "") {
            return;
        }
        if (event.getBlock().isBlockPowered() || event.getBlock().isBlockIndirectlyPowered()) {
            try {
                for (Location receiver : plugin.WireBox.getReceiverLocations(signObject.getLine(1))) {
                    if (receiver.getBlock().getType() == Material.SIGN_POST) {
                        if (receiver.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                            this.plugin.WireBox.removeReceiverAt(receiver, false);
                        }
                        receiver.getBlock().setTypeIdAndData(Material.REDSTONE_TORCH_ON.getId(), (byte) 0x5, true);
                    } else if (receiver.getBlock().getType() == Material.WALL_SIGN) {
                        if (receiver.getBlock().getData() == 0x2) {
                            if (receiver.getBlock().getRelative(BlockFace.WEST).getType() == Material.AIR) {
                                this.plugin.WireBox.removeReceiverAt(receiver, false);
                            }
                            receiver.getBlock().setTypeIdAndData(Material.REDSTONE_TORCH_ON.getId(), (byte) 0x4, true);
                        } else if (receiver.getBlock().getData() == 0x3) {
                            if (receiver.getBlock().getRelative(BlockFace.EAST).getType() == Material.AIR) {
                                this.plugin.WireBox.removeReceiverAt(receiver, false);
                            }
                            receiver.getBlock().setTypeIdAndData(Material.REDSTONE_TORCH_ON.getId(), (byte) 0x3, true);
                        } else if (receiver.getBlock().getData() == 0x4) {
                            if (receiver.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.AIR) {
                                this.plugin.WireBox.removeReceiverAt(receiver, false);
                            }
                            receiver.getBlock().setTypeIdAndData(Material.REDSTONE_TORCH_ON.getId(), (byte) 0x2, true);
                        } else if (receiver.getBlock().getData() == 0x5) {
                            if (receiver.getBlock().getRelative(BlockFace.NORTH).getType() == Material.AIR) {
                                this.plugin.WireBox.removeReceiverAt(receiver, false);
                            }
                            receiver.getBlock().setTypeIdAndData(Material.REDSTONE_TORCH_ON.getId(), (byte) 0x1, true);
                        } else {
                            WirelessRedstone.getLogger().info("Weirdest sign Efar!");
                        }
                    }
                }
            } catch (Exception e) {
                WirelessRedstone.getLogger().severe(e.getMessage());
                return;
            }
        } else if (!event.getBlock().isBlockPowered()) try {
            for (WirelessReceiver receiver : plugin.WireBox.getChannel(signObject.getLine(1)).getReceivers()) {
                Location rloc = plugin.WireBox.getPointLocation(receiver);
                Block othersign = rloc.getBlock();
                othersign.setType(Material.AIR);
                if (receiver.getisWallSign()) {
                    othersign.setType(Material.WALL_SIGN);
                    othersign.setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) receiver.getDirection(), true);
                } else {
                    othersign.setType(Material.SIGN_POST);
                    othersign.setTypeIdAndData(Material.SIGN_POST.getId(), (byte) receiver.getDirection(), true);
                }
                if (othersign.getState() instanceof Sign) {
                    Sign signtemp = (Sign) othersign.getState();
                    signtemp.setLine(0, "[WRr]");
                    signtemp.setLine(1, signObject.getLine(1));
                    signtemp.update(true);
                }
            }
        } catch (Exception e) {
            WirelessRedstone.getLogger().severe(e.getMessage());
            return;
        }
    }
