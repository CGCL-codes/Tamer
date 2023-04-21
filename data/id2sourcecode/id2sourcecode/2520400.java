    public void init(String soundsdesc) {
        URL url = SoundFactory.class.getResource(soundsdesc);
        try {
            JAXBContext context = JAXBContext.newInstance("elf.xml.sounds");
            Unmarshaller unmarshaller = context.createUnmarshaller();
            SoundsBaseType root = null;
            Object tmpobj = unmarshaller.unmarshal(url.openConnection().getInputStream());
            if (tmpobj instanceof JAXBElement<?>) {
                if (((JAXBElement<?>) tmpobj).getValue() instanceof SoundsBaseType) {
                    root = (SoundsBaseType) ((JAXBElement<?>) tmpobj).getValue();
                    addSound("E_DROPGIFT", root.getEnemyDropgift().getSoundpath());
                    addSound("E_EXPLODE", root.getEnemyExplode().getSoundpath());
                    addSound("E_HURT", root.getEnemyHurt().getSoundpath());
                    addSound("E_SHOT", root.getEnemyShot().getSoundpath());
                    addSound("P_DIE", root.getPlayerDie().getSoundpath());
                    addSound("P_FALL", root.getPlayerFall().getSoundpath());
                    addSound("P_HURT", root.getPlayerHurt().getSoundpath());
                    addSound("P_SHOT", root.getPlayerShot().getSoundpath());
                    addSound("P_TAKEITEM", root.getPlayerTakeitem().getSoundpath());
                    addSound("A_DIE", root.getAnimalDie().getSoundpath());
                    addSound("S_CHANGEITEM", root.getShopChangeitem().getSoundpath());
                    addSound("S_CHANGEPAGE", root.getShopChangepage().getSoundpath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
