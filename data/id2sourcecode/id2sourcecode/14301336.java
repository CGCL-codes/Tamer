    protected boolean validSysAndUnitPanel() {
        String filename = ((NewSaadaDBTool) (frame)).saada_home + Database.getSepar() + "config" + Database.getSepar() + "saadadb.xml";
        Messenger.printMsg(Messenger.TRACE, "Save SaadaDB config in <" + filename + ">");
        try {
            String dbname = saadadb_name.getText().trim();
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\" standalone=\"no\"?>\n");
            bw.write("<!DOCTYPE saadadb SYSTEM \"saadadb.dtd\">\n");
            bw.write("<saadadb>\n");
            bw.write("    <database>\n");
            bw.write("        <name><![CDATA[" + dbname + "]]></name>\n");
            bw.write("        <description><![CDATA[]]></description>\n");
            bw.write("        <root_dir><![CDATA[" + (new File(saadadb_home.getText().trim(), dbname)).getAbsolutePath() + "]]></root_dir>\n");
            bw.write("        <repository_dir><![CDATA[" + (new File(saadadb_rep.getText().trim(), dbname)).getAbsolutePath() + "]]></repository_dir>\n");
            bw.write("    </database>\n");
            bw.write("    <relational_database>\n");
            bw.write("        <name><![CDATA[" + dbms_database_name.getText().trim() + "]]></name>\n");
            bw.write("        <administrator>\n");
            bw.write("            <name><![CDATA[" + dbms_admin.getText().trim() + "]]></name>\n");
            bw.write("        </administrator>\n");
            bw.write("        <reader>\n");
            bw.write("            <name><![CDATA[" + dbms_reader.getText().trim() + "]]></name>\n");
            bw.write("            <password><![CDATA[" + new String(dbms_reader_passwd.getPassword()) + "]]></password>\n");
            bw.write("        </reader>\n");
            if (dbmswrapper == null) {
                JOptionPane.showMessageDialog(frame, "DBMS wrapper not set, please fill all panels", "Configuration Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            bw.write("        <jdbc_driver>" + dbmswrapper.getDriver() + "</jdbc_driver>\n");
            bw.write("        <jdbc_url>" + dbmswrapper.getJdbcURL((new File(saadadb_rep.getText().trim(), dbname)).getAbsolutePath(), dbms_database_name.getText().trim()) + "</jdbc_url>\n");
            bw.write("    </relational_database>\n");
            bw.write("    <web_interface>\n");
            bw.write("        <webapp_home><![CDATA[" + (new File(tomcat_home.getText().trim())).getAbsolutePath() + "]]></webapp_home>\n");
            bw.write("        <url_root><![CDATA[" + url_root.getText().trim() + "]]></url_root>\n");
            bw.write("    </web_interface>\n");
            bw.write("    <spectral_coordinate>\n");
            for (int i = 0; i < channel_button.length; i++) {
                if (channel_button[i].isSelected()) {
                    String spsys = "channel";
                    if (spec_syst != null && spec_syst.getSelectedItem() != null) {
                        spsys = spec_syst.getSelectedItem().toString();
                    }
                    bw.write("        <abscisse type=\"" + channel_button[i].getText().toUpperCase() + "\" unit=\"" + spsys + "\"/>\n");
                    break;
                }
            }
            bw.write("    </spectral_coordinate>\n");
            bw.write("    <coordinate_system>\n");
            String[] se = coord_syst.getSelectedItem().toString().split(",");
            bw.write("        <system>" + se[0] + "</system>\n");
            if (se.length == 2) {
                bw.write("        <equinox>" + se[1] + "</equinox>\n");
            } else {
                bw.write("        <equinox></equinox>\n");
            }
            bw.write("    </coordinate_system>\n");
            bw.write("</saadadb>\n");
            bw.close();
        } catch (IOException e) {
            Messenger.printStackTrace(e);
            JOptionPane.showMessageDialog(frame, "Writing saadadb confif file: " + e.getMessage(), "Configuration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
