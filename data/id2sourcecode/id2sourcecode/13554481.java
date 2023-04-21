    private String buildUpdate(Player player) {
        switch(type) {
            case TYPE_ADVANCEMENTS:
                return AdvancementTools.getPlayerAdvancements(null, player).toString();
            case TYPE_CONTRACTS_STATE:
                return ContractTools.getContractsState(null, player).toString();
            case TYPE_PLAYER_CONTRACTS:
                return ContractTools.getPlayerContracts(null, player).toString();
            case TYPE_INFORMATION:
                return new JSONStringer().value((String) args[0]).toString();
            case TYPE_EFFECT:
                return EffectTools.getEffect(null, (Effect) args[0]).toString();
            case TYPE_PLAYER_FLEETS:
                return FleetTools.getPlayerFleets(null, player).toString();
            case TYPE_PLAYER_FLEET:
                return FleetTools.getPlayerFleet(null, (Integer) args[0]).toString();
            case TYPE_PLAYER_SYSTEMS:
                return SystemTools.getPlayerSystems(null, player).toString();
            case TYPE_PLAYER_GENERATORS:
                return StructureTools.getPlayerGenerators(null, player).toString();
            case TYPE_XP:
                return XpTools.getPlayerXp(null, player).toString();
            case TYPE_SERVER_SHUTDOWN:
                return String.valueOf(args[0]);
            case TYPE_CHAT_CHANNELS:
                return ChatTools.getChannels(null, player).toString();
            case TYPE_NEW_EVENT:
            case TYPE_NEW_NEWS:
            case TYPE_NEW_MESSAGE:
                return "\"\"";
            case TYPE_CHAT:
                return (String) args[0];
            case TYPE_PRODUCTS:
                return ProductTools.getPlayerProducts(null, player).toString();
            case TYPE_AREA:
                try {
                    if (player.getIdCurrentArea() != 0) return AreaTools.getArea(null, DataAccess.getAreaById(player.getIdCurrentArea()), player).toString();
                } catch (Exception e) {
                    LoggingSystem.getServerLogger().warn("Could not build area update.", e);
                }
                return null;
            case TYPE_ALLY:
                try {
                    return AllyTools.getAlly(null, player, (Long) args[0]).toString();
                } catch (Exception e) {
                    LoggingSystem.getServerLogger().warn("Could not build ally update.", e);
                }
                return null;
            default:
                throw new IllegalStateException("No implementation defined for update: '" + type + "'.");
        }
    }
