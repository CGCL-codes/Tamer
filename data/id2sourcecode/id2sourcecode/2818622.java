    private void btnWeaponCostActionPerformed(java.awt.event.ActionEvent evt) {
        int checkRow = tblWeapon.getSelectedRow();
        if (checkRow != -1) {
            double dmg = Double.parseDouble(docWeapon.table[checkRow][1]);
            double pen = Double.parseDouble(docWeapon.table[checkRow][2]);
            double acc = Double.parseDouble(docWeapon.table[checkRow][3]);
            double round = Double.parseDouble(docWeapon.table[checkRow][4]);
            double blast = Double.parseDouble(docWeapon.table[checkRow][5]);
            double hitChance = acc;
            double missChance = 1 - hitChance;
            double penChance = pen / 100;
            double bounceChance = 1 - penChance;
            double blastSize = (blast + 1) / 2;
            double hitDmg = dmg * blastSize;
            double bounceDmg = dmg * blastSize * penChance;
            double missDmg = dmg * (blastSize - 1) * penChance;
            double attack = ((hitChance * (penChance * hitDmg + bounceChance * bounceDmg) + missChance * missDmg) * round);
            int cost = (int) Math.round(attack * 20);
            txtWeaponCost.setText("Attack:" + String.valueOf(attack) + "   Suggest Cost:" + String.valueOf(cost));
        }
    }
