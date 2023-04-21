    public void givePrice(Player player) {
        PlayerInventory inventory;
        int blockId;
        int blockAmount;
        blockId = Main.getInstance().getConfigHandler().reactionBlockId.get(priceId);
        blockAmount = Main.getInstance().getConfigHandler().reactionBlockAmount.get(priceId);
        if (Material.getMaterial(blockId) == null) {
            System.out.println("* Error: Invalid item, visit http://www.minecraftwiki.net/wiki/Data_values#Block_IDs_.28Minecraft_Beta.29 for more information.");
            return;
        }
        inventory = player.getInventory();
        ItemStack newItem = new ItemStack(blockId);
        newItem.setAmount(blockAmount);
        inventory.addItem(newItem);
        StringBuilder winMessageBuilder = new StringBuilder();
        winMessageBuilder.append(ChatColor.GREEN);
        winMessageBuilder.append("You got ");
        winMessageBuilder.append(blockAmount);
        winMessageBuilder.append(" ");
        winMessageBuilder.append(Main.getInstance().getConfigHandler().reactionBlockName.get(priceId));
        winMessageBuilder.append(".");
        player.sendMessage(winMessageBuilder.toString());
    }
