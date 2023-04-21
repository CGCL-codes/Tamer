    public static void Save(EpicZone zone) {
        if (zone != null && zone.getTag().length() > 0) {
            Yaml yaml = new Yaml();
            File file = new File(General.plugin.getDataFolder() + File.separator + PATH + File.separator + zone.getTag() + ".yml");
            HashMap<String, Object> root = new HashMap<String, Object>();
            FileOutputStream stream;
            BufferedWriter writer;
            root.put("name", zone.getName());
            root.put("type", zone.getType().toString());
            root.put("radius", zone.getRadius());
            root.put("world", zone.getWorld());
            root.put("entertext", zone.getEnterText());
            root.put("exittext", zone.getExitText());
            root.put("floor", zone.getFloor());
            root.put("ceiling", zone.getCeiling());
            root.put("pvp", zone.getPVP());
            root.put("mobs", zone.getMobs().toArray());
            root.put("fire", zone.getFire());
            root.put("sanctuary", zone.getSanctuary());
            root.put("fireburnsmobs", zone.getFireBurnsMobs());
            Map<String, Object> explode = new TreeMap<String, Object>();
            explode.put("tnt", zone.getExplode().getTNT());
            explode.put("creeper", zone.getExplode().getCreeper());
            explode.put("ghast", zone.getExplode().getGhast());
            root.put("explode", explode);
            Map<String, Object> fire = new TreeMap<String, Object>();
            fire.put("ignite", zone.getFire().getIgnite());
            fire.put("spread", zone.getFire().getSpread());
            root.put("fire", fire);
            Map<String, Object> regen = new TreeMap<String, Object>();
            regen.put("amount", zone.getRegen().getAmount());
            regen.put("delay", zone.getRegen().getDelay());
            regen.put("interval", zone.getRegen().getInterval());
            regen.put("maxregen", zone.getRegen().getMaxRegen());
            regen.put("mindegen", zone.getRegen().getMinDegen());
            regen.put("restdelay", zone.getRegen().getRestDelay());
            regen.put("bedbonus", zone.getRegen().getBedBonus());
            root.put("regen", regen);
            root.put("owners", zone.getOwners());
            root.put("disallowedcommands", zone.getDisallowedCommands());
            root.put("childzones", zone.getChildrenTags().toArray());
            root.put("points", zone.getPoints());
            root.put("permissions", BuildPerms(zone.getPermissions()));
            try {
                if (!file.exists()) {
                    File dir = new File(General.plugin.getDataFolder() + File.separator + PATH);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    file.createNewFile();
                }
                stream = new FileOutputStream(file);
                stream.getChannel().truncate(0);
                writer = new BufferedWriter(new OutputStreamWriter(stream));
                try {
                    writer.write(yaml.dump(root));
                } finally {
                    writer.close();
                }
            } catch (IOException e) {
                Log.Write(e.getMessage());
            }
        }
    }
