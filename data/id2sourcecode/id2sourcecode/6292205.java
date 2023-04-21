    public void actionPerformed(ActionEvent e) {
        Component c = (Component) e.getSource();
        try {
            if (c == list) {
                if (cmd != LIST) {
                    avg.setEnabled(false);
                    infos.setEnabled(false);
                    infof.setEnabled(false);
                    cmd = LIST;
                    CS_Search css = new CS_Search();
                    css.setSize(500, 100);
                    css.setVisible(true);
                } else {
                    list.setEnabled(true);
                    avg.setEnabled(true);
                    infos.setEnabled(true);
                    infof.setEnabled(true);
                    stuid.setEditable(false);
                    cmd = NONE;
                }
            } else if (c == avg) {
                rs = stat.executeQuery("SELECT AVG(Midterm) FROM Student_management");
                display.setText("========================================================\n");
                display.append("\t\t�߰���� ��� ����\n");
                display.append("========================================================\n");
                while (rs.next()) {
                    int val = rs.getInt(1);
                    display.append("\t\t\t" + val + "\n\n");
                }
                rs = stat.executeQuery("SELECT AVG(Final) FROM Student_management");
                display.append("========================================================\n");
                display.append("\t\t�⸻��� ��� ����\n");
                display.append("========================================================\n");
                while (rs.next()) {
                    int val = rs.getInt(1);
                    display.append("\t\t\t" + val + "\n\n");
                }
                rs = stat.executeQuery("SELECT AVG((Midterm+Final)/2) FROM Student_management");
                display.append("========================================================\n");
                display.append("\t\t�߰��⸻ ��� ����\n");
                display.append("========================================================\n");
                while (rs.next()) {
                    int val = rs.getInt(1);
                    display.append("\t\t\t" + val + "");
                }
                cmd = NONE;
                initialize();
            } else if (c == infos) {
                if (cmd != INFOS) {
                    list.setEnabled(false);
                    avg.setEnabled(false);
                    infof.setEnabled(false);
                    stuid.setEditable(true);
                    cmd = INFOS;
                } else {
                    String id_string = stuid.getText().trim();
                    rs = stat.executeQuery("select * from Student_management where StudentID = '" + id_string + "';");
                    display.setText("===================================================================================================================================" + "\n");
                    display.append("\t�й� \t\t �̸� \t\t ���� \t\t ������ \t\t\t�߰� ���� \t�⸻ ���� \t �� ����\n");
                    display.append("===================================================================================================================================" + "\n");
                    while (rs.next()) {
                        id_string = rs.getString(1);
                        String name = rs.getString(2);
                        String sex = rs.getString(3);
                        String year = rs.getString(4);
                        String mon = rs.getString(5);
                        String day = rs.getString(6);
                        int mi = rs.getInt(7);
                        int fi = rs.getInt(8);
                        int full = (mi + fi) / 2;
                        display.append("\t" + id_string + " \t\t" + name + "\t\t" + sex + "\t\t" + year + "." + mon + "." + day + "" + "\t " + mi + "\t" + fi + "\t" + full + "\n");
                        display.append("-----------------------------------------------------------------------------------------------------------------------------------\n");
                        cmd = NONE;
                    }
                    list.setEnabled(true);
                    avg.setEnabled(true);
                    infos.setEnabled(true);
                    infof.setEnabled(true);
                    stuid.setEditable(false);
                    cmd = NONE;
                }
            } else if (c == infof) {
                rs = stat.executeQuery("select * from Professor_management;");
                display.setText("=======================================================================================" + "\n");
                display.append("�̸� \t\t ���� \t ��� \t\t �� \t\t �� \n");
                display.append("=======================================================================================" + "\n");
                while (rs.next()) {
                    String pf_name = rs.getString(1);
                    String gender = rs.getString(2);
                    String year = rs.getString(3);
                    String month = rs.getString(4);
                    String day = rs.getString(5);
                    display.append(pf_name + "\t\t" + gender + "\t" + year + "\t\t" + month + "\t\t" + day + "\n");
                }
                initialize();
            } else if (c == cancel) {
                list.setEnabled(true);
                avg.setEnabled(true);
                infos.setEnabled(true);
                infof.setEnabled(true);
                stuid.setEditable(false);
                cmd = NONE;
            }
        } catch (Exception ex) {
        }
        return;
    }
