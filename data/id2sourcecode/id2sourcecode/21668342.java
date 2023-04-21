    public void save(ObjectOutputStream so) throws IOException {
        so.writeUTF(picname);
        so.writeInt(subsquare);
        so.writeInt(number);
        so.writeUTF(name);
        so.writeUTF(lastname);
        so.writeInt(maxhealth);
        so.writeInt(health);
        so.writeInt(maxstamina);
        so.writeInt(stamina);
        so.writeInt(maxmana);
        so.writeInt(mana);
        so.writeFloat(load);
        so.writeInt(food);
        so.writeInt(water);
        so.writeInt(strength);
        so.writeInt(vitality);
        so.writeInt(dexterity);
        so.writeInt(intelligence);
        so.writeInt(wisdom);
        so.writeInt(defense);
        so.writeInt(magicresist);
        so.writeInt(strengthboost);
        so.writeInt(vitalityboost);
        so.writeInt(dexterityboost);
        so.writeInt(intelligenceboost);
        so.writeInt(wisdomboost);
        so.writeInt(defenseboost);
        so.writeInt(magicresistboost);
        so.writeInt(flevel);
        so.writeInt(nlevel);
        so.writeInt(plevel);
        so.writeInt(wlevel);
        so.writeInt(flevelboost);
        so.writeInt(nlevelboost);
        so.writeInt(plevelboost);
        so.writeInt(wlevelboost);
        so.writeInt(fxp);
        so.writeInt(nxp);
        so.writeInt(pxp);
        so.writeInt(wxp);
        so.writeBoolean(isdead);
        so.writeBoolean(wepready);
        so.writeBoolean(ispoisoned);
        if (ispoisoned) {
            so.writeInt(poison);
            so.writeInt(poisoncounter);
        }
        so.writeBoolean(silenced);
        if (silenced) so.writeInt(silencecount);
        so.writeBoolean(hurtweapon);
        so.writeBoolean(hurthand);
        so.writeBoolean(hurthead);
        so.writeBoolean(hurttorso);
        so.writeBoolean(hurtlegs);
        so.writeBoolean(hurtfeet);
        so.writeInt(timecounter);
        so.writeInt(walkcounter);
        so.writeInt(spellcount);
        so.writeInt(weaponcount);
        so.writeInt(kuswordcount);
        so.writeInt(rosbowcount);
        so.writeUTF(currentspell);
        if (abilities != null) {
            so.writeInt(abilities.length);
            for (int j = 0; j < abilities.length; j++) {
                abilities[j].save(so);
            }
        } else so.writeInt(0);
        if (weapon.name.equals("Fist/Foot")) so.writeBoolean(false); else {
            so.writeBoolean(true);
            so.writeObject(weapon);
        }
        if (hand == null) so.writeBoolean(false); else {
            so.writeBoolean(true);
            so.writeObject(hand);
        }
        if (head == null) so.writeBoolean(false); else {
            so.writeBoolean(true);
            so.writeObject(head);
        }
        if (torso == null) so.writeBoolean(false); else {
            so.writeBoolean(true);
            so.writeObject(torso);
        }
        if (legs == null) so.writeBoolean(false); else {
            so.writeBoolean(true);
            so.writeObject(legs);
        }
        if (feet == null) so.writeBoolean(false); else {
            so.writeBoolean(true);
            so.writeObject(feet);
        }
        if (neck == null) so.writeBoolean(false); else {
            so.writeBoolean(true);
            so.writeObject(neck);
        }
        if (pouch1 == null) so.writeBoolean(false); else {
            so.writeBoolean(true);
            so.writeObject(pouch1);
        }
        if (pouch2 == null) so.writeBoolean(false); else {
            so.writeBoolean(true);
            so.writeObject(pouch2);
        }
        so.writeObject(quiver);
        so.writeObject(pack);
    }
