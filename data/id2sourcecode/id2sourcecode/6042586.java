    public int getOverallAverage() {
        double common = getCommonPlayerAttributesAverage();
        double player = getSpecificPlayerAttributesAverage();
        double totalPlayerAttributes = (common + player) / 2;
        double form = getVIAPlayerAttribute(PlayerAttributes.VIA_FORM_KEY).getValue();
        double energy = getVIAPlayerAttribute(PlayerAttributes.VIA_ENERGY_KEY).getValue();
        double motivation = getVIAPlayerAttribute(PlayerAttributes.VIA_MORALE_KEY).getValue();
        return (int) (totalPlayerAttributes + form + energy + motivation) / 4;
    }
