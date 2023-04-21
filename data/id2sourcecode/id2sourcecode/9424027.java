    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (LOAD1.equals(command)) {
            JComboBox cb = (JComboBox) e.getSource();
            int j = cb.getSelectedIndex() - 1;
            if ((j >= 0) && (trigger)) {
                loadScheme1(j);
            }
        }
        if (LOAD2.equals(command)) {
            JComboBox cb = (JComboBox) e.getSource();
            int j = cb.getSelectedIndex() - 1;
            if ((j >= 0) && (trigger)) {
                loadScheme2(j);
            }
        }
        if (DELETE1.equals(command)) {
            if (trigger) {
                JComboBox cb = (JComboBox) e.getSource();
                int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this scheme?\n" + "(This action cannot be reversed)", "Confirm Scheme Overwrite", JOptionPane.YES_NO_OPTION);
                if (n == 0) {
                    trigger = false;
                    int j = cb.getSelectedIndex() - 1;
                    if (j >= 0) {
                        deleteScheme1(j);
                    }
                    trigger = true;
                }
            }
        }
        if (DELETE2.equals(command)) {
            if (trigger) {
                JComboBox cb = (JComboBox) e.getSource();
                int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this scheme?\n" + "(This action cannot be reversed)", "Confirm Scheme Overwrite", JOptionPane.YES_NO_OPTION);
                if (n == 0) {
                    trigger = false;
                    int j = cb.getSelectedIndex() - 1;
                    if (j >= 0) {
                        deleteScheme2(j);
                    }
                    trigger = true;
                }
            }
        }
        if (SAVE1.equals(command)) {
            trigger = false;
            String s = "INSERT INTO comparescheme (name, song1, song2, max_score, syll_comp, song_comp)VALUES (";
            System.out.println("SAVE 1 ENTERED");
            String b = " , ";
            String t = ")";
            String question = "Enter name for experiment:";
            String name = JOptionPane.showInputDialog(question);
            System.out.println("here");
            boolean existsAlready = ac.dbc.testScheme(name);
            if (existsAlready) {
                int n = JOptionPane.showConfirmDialog(null, "That name already exists in the database\n" + "Do you want to remove it?", "Confirm Scheme Overwrite", JOptionPane.YES_NO_OPTION);
                if (n == 0) {
                    ac.dbc.writeToDataBase("DELETE FROM comparescheme WHERE name='" + name + "'");
                    existsAlready = false;
                }
            }
            System.out.println("here " + existsAlready);
            if (!existsAlready) {
                ac.dbc.writeToDataBase("DELETE FROM comparescheme WHERE name='" + name + "'");
                int be1 = 0;
                int bs1 = 0;
                int[][] table = ca.table;
                for (int i = 0; i < table.length; i++) {
                    for (int j = 0; j < table.length; j++) {
                        if (table[i][j] == 1) {
                            String s2 = s + "'" + name + "'" + b + ac.archIds[j] + b + ac.archIds[i] + b + 1 + b + be1 + b + bs1 + t;
                            System.out.println(s2);
                            ac.dbc.writeToDataBase(s2);
                        }
                    }
                }
            }
            makeSchemesList1();
            trigger = true;
        }
        if (SAVE2.equals(command)) {
            trigger = false;
            String s = "INSERT INTO comparescheme (name, song1, song2, max_score, syll_comp, song_comp)VALUES (";
            System.out.println("SAVE 2 ENTERED");
            String b = " , ";
            String t = ")";
            int def = -1;
            String question = "Enter name for experiment:";
            String name = JOptionPane.showInputDialog(question);
            if (name != null) {
                boolean existsAlready = ac.dbc.testScheme(name);
                if (existsAlready) {
                    int n = JOptionPane.showConfirmDialog(this, "That name already exists in the database\n" + "Do you want to remove it?", "Confirm Scheme Overwrite", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                        ac.dbc.writeToDataBase("DELETE FROM comparescheme WHERE name='" + name + "'");
                        existsAlready = false;
                    }
                }
                if (!existsAlready) {
                    int be1 = 0;
                    int bs1 = 0;
                    for (int i = 0; i < sa.leftList.size(); i++) {
                        Integer q = (Integer) sa.leftList.get(i);
                        for (int j = 0; j < ac.archIds.length; j++) {
                            if (q.intValue() == ac.archIds[j]) {
                                String s2 = s + "'" + name + "'" + b + ac.archIds[j] + b + def + b + def + b + def + b + def + t;
                                System.out.println(s2);
                                ac.dbc.writeToDataBase(s2);
                            }
                        }
                    }
                }
                makeSchemesList2();
                trigger = true;
            }
        }
    }
