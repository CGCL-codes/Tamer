    public void RemoveEquipment(EquipmentType type, int slot) {
        Equipment[] equip = EquipmentByType(type);
        int last = equip.length - 1;
        for (int i = slot; i < last; i++) {
            equip[i] = equip[i + 1];
        }
        equip[last] = null;
    }
