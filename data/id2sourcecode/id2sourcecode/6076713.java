    private void update_field(String action) {
        query = "";
        this.setTitle(LangCenter.getString(action) + " " + LangCenter.getString("Testcase"));
        if (action.equals("Add")) {
            tf_TestcaseName.setBackground(Color.WHITE);
            tf_TestcaseName.setEditable(true);
            tf_TestcaseKeyword.setText("");
            tf_TestcaseKeyword.setBackground(new Color(230, 230, 230));
            b_AddKeyword.setVisible(true);
            tp_TestcaseInformation.setText("");
            tp_TestcaseInformation.setBackground(Color.WHITE);
            tp_TestcaseInformation.setEditable(true);
            tp_TestcaseInformation.setCaretPosition(0);
            tp_TestcaseCIU.setText("");
            tp_TestcaseCIU.setBackground(Color.WHITE);
            tp_TestcaseCIU.setEditable(true);
            tp_TestcaseCIU.setCaretPosition(0);
            l_Image1.setText(LangCenter.getString("Image") + " 1:");
            l_Image1.setIcon(null);
            l_Image1.setBorder(null);
            l_Image1.setForeground(Color.BLACK);
            l_Image1.setVisible(true);
            l_Image1.setBounds(new Rectangle(10, 200, 100, 20));
            tf_Image1.setVisible(true);
            tf_Image1.setText("");
            l_Image2.setText(LangCenter.getString("Image") + " 2:");
            l_Image2.setIcon(null);
            l_Image2.setBorder(null);
            l_Image2.setForeground(Color.BLACK);
            l_Image2.setVisible(false);
            l_Image2.setBounds(new Rectangle(10, 220, 100, 20));
            tf_Image2.setVisible(false);
            tf_Image2.setText("");
            l_Image3.setText(LangCenter.getString("Image") + " 3:");
            l_Image3.setIcon(null);
            l_Image3.setBorder(null);
            l_Image3.setForeground(Color.BLACK);
            l_Image3.setVisible(false);
            l_Image3.setBounds(new Rectangle(10, 240, 100, 20));
            tf_Image3.setVisible(false);
            tf_Image3.setText("");
            b_AddImage.setVisible(true);
            tp_TestcaseTestBody.setBackground(Color.WHITE);
            tp_TestcaseTestBody.setEditable(true);
            tp_TestcaseTestBody.setText("");
            scrollTestBody.setBounds(new Rectangle(10, 290, 570, 270));
            tp_TestcaseTestBody.setCaretPosition(0);
            l_TestcaseOptionalTest.setVisible(false);
            tp_TestcaseOptionalTest.setEnabled(false);
            scrollOptionalTest.setVisible(false);
            chb_EnableOptionalTest.setVisible(true);
            tp_TestcaseOptionalTest.setBackground(Color.WHITE);
            tp_TestcaseOptionalTest.setCaretPosition(0);
            b_Edit.setVisible(false);
            l_Status.setVisible(false);
        } else if (action.equals("View")) {
            query = Database.executed_query("SELECT Name " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short");
            tf_TestcaseName.setText(query);
            tf_TestcaseName.setBackground(new Color(240, 240, 240));
            tf_TestcaseName.setEditable(false);
            query = Database.executed_query("SELECT Keyword " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short");
            String keywords = "";
            st = new StringTokenizer(query, ";");
            while (st.countTokens() != 0) {
                keywords += Database.executed_query("SELECT Name " + "FROM keywords " + "WHERE ID='" + st.nextToken() + "';", "short") + "; ";
            }
            tf_TestcaseKeyword.setText(keywords);
            tf_TestcaseKeyword.setBackground(new Color(200, 255, 200));
            b_AddKeyword.setVisible(false);
            query = Database.executed_query("SELECT Information " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short");
            tp_TestcaseInformation.setText(query);
            tp_TestcaseInformation.setBackground(new Color(240, 240, 240));
            tp_TestcaseInformation.setEditable(false);
            tp_TestcaseInformation.setCaretPosition(0);
            query = Database.executed_query("SELECT Configuration_in_use " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short");
            tp_TestcaseCIU.setText(query);
            tp_TestcaseCIU.setBackground(new Color(240, 240, 240));
            tp_TestcaseCIU.setEditable(false);
            tp_TestcaseCIU.setCaretPosition(0);
            l_Image1.setText("");
            l_Image1.setForeground(Color.BLACK);
            tf_Image1.setVisible(false);
            l_Image2.setText("");
            l_Image2.setForeground(Color.BLACK);
            tf_Image2.setVisible(false);
            l_Image3.setText("");
            l_Image3.setForeground(Color.BLACK);
            tf_Image3.setVisible(false);
            b_AddImage.setVisible(false);
            query = Database.executed_query("SELECT Image " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short");
            st = new StringTokenizer(query, "|");
            int count = st.countTokens();
            for (int i = 0; i < count; i++) {
                url[i] = st.nextToken();
            }
            if (!url[0].equals("")) {
                try {
                    (new URL(url[0])).openConnection().getContent();
                    l_Image1.setIcon(GlobalResources.CreateLittleImage(url[0], l_Image1, "1"));
                    l_Image1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    l_Image1.addMouseListener(this);
                } catch (IOException e) {
                    l_Image1.setText(" " + LangCenter.getString("Error"));
                    l_Image1.setBorder(BorderFactory.createLineBorder(Color.RED));
                    l_Image1.setForeground(Color.RED);
                }
                l_Image1.setToolTipText(url[0]);
                l_Image1.setName("Image1");
                l_Image1.setBounds(new Rectangle(10, 200, 70, 70));
            }
            if (!url[1].equals("")) {
                try {
                    (new URL(url[1])).openConnection().getContent();
                    l_Image2.setIcon(GlobalResources.CreateLittleImage(url[1], l_Image2, "2"));
                    l_Image2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    l_Image2.addMouseListener(this);
                } catch (IOException e) {
                    l_Image2.setText(" " + LangCenter.getString("Error"));
                    l_Image2.setBorder(BorderFactory.createLineBorder(Color.RED));
                    l_Image2.setForeground(Color.RED);
                }
                l_Image2.setToolTipText(url[1]);
                l_Image2.setName("Image2");
                l_Image2.setBounds(new Rectangle(110, 200, 70, 70));
            }
            if (!url[2].equals("")) {
                try {
                    (new URL(url[2])).openConnection().getContent();
                    l_Image3.setIcon(GlobalResources.CreateLittleImage(url[2], l_Image3, "3"));
                    l_Image3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    l_Image3.addMouseListener(this);
                } catch (IOException e) {
                    l_Image3.setText(" " + LangCenter.getString("Error"));
                    l_Image3.setBorder(BorderFactory.createLineBorder(Color.RED));
                    l_Image3.setForeground(Color.RED);
                }
                l_Image3.setToolTipText(url[2]);
                l_Image3.setName("Image3");
                l_Image3.setBounds(new Rectangle(210, 200, 70, 70));
            }
            query = Database.executed_query("SELECT Test_body " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short");
            tp_TestcaseTestBody.setText(query);
            tp_TestcaseTestBody.setBackground(new Color(240, 240, 240));
            scrollTestBody.setBounds(new Rectangle(10, 290, 570, 370));
            tp_TestcaseTestBody.setEditable(false);
            tp_TestcaseTestBody.setCaretPosition(0);
            l_TestcaseOptionalTest.setVisible(false);
            tp_TestcaseOptionalTest.setEnabled(false);
            scrollOptionalTest.setVisible(false);
            chb_EnableOptionalTest.setVisible(false);
            query = Database.executed_query("SELECT Optional_test " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short");
            if (!query.equals("")) {
                l_TestcaseOptionalTest.setVisible(true);
                scrollTestBody.setBounds(new Rectangle(10, 290, 570, 270));
                tp_TestcaseOptionalTest.setEnabled(true);
                scrollOptionalTest.setVisible(true);
                tp_TestcaseOptionalTest.setEditable(false);
                tp_TestcaseOptionalTest.setBackground(new Color(240, 240, 240));
                tp_TestcaseOptionalTest.setText(query);
                tp_TestcaseOptionalTest.setCaretPosition(0);
            }
            if (GlobalResources.Add_testcase.equals("1")) {
                b_Edit.setVisible(true);
            }
            l_Status.setVisible(true);
            String Status = "";
            int StatusID = Integer.parseInt(Database.executed_query("SELECT Status " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short"));
            if (StatusID == 0) {
                Status = LangCenter.getString("Disabled");
            } else if (StatusID == 1) {
                Status = LangCenter.getString("Active");
            }
            l_Status.setText(LangCenter.getString("Status") + ": " + Status);
        } else if (action.equals("Edit")) {
            query = Database.executed_query("SELECT Name " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short");
            tf_TestcaseName.setText(query);
            tf_TestcaseName.setBackground(Color.WHITE);
            tf_TestcaseName.setEditable(true);
            query = Database.executed_query("SELECT Keyword " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short");
            String keywords = "";
            st = new StringTokenizer(query, ";");
            while (st.countTokens() != 0) {
                keywords += Database.executed_query("SELECT Name " + "FROM keywords " + "WHERE ID='" + st.nextToken() + "';", "short") + "; ";
            }
            tf_TestcaseKeyword.setText(keywords);
            tf_TestcaseKeyword.setBackground(new Color(230, 230, 230));
            b_AddKeyword.setVisible(true);
            query = Database.executed_query("SELECT Information " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short");
            tp_TestcaseInformation.setText(query);
            tp_TestcaseInformation.setBackground(Color.WHITE);
            tp_TestcaseInformation.setEditable(true);
            tp_TestcaseInformation.setCaretPosition(0);
            query = Database.executed_query("SELECT Configuration_in_use " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short");
            tp_TestcaseCIU.setText(query);
            tp_TestcaseCIU.setBackground(Color.WHITE);
            tp_TestcaseCIU.setEditable(true);
            tp_TestcaseCIU.setCaretPosition(0);
            b_AddImage.setVisible(false);
            query = Database.executed_query("SELECT Image " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short");
            st = new StringTokenizer(query, "|");
            int count = st.countTokens();
            for (int i = 0; i < count; i++) {
                url[i] = st.nextToken();
            }
            l_Image1.setText(LangCenter.getString("Image") + " 1:");
            l_Image1.setIcon(null);
            l_Image1.setBorder(null);
            l_Image1.setForeground(Color.BLACK);
            l_Image1.setBounds(new Rectangle(10, 200, 100, 20));
            l_Image1.setVisible(true);
            tf_Image1.setText(url[0]);
            tf_Image1.setVisible(true);
            l_Image2.setText(LangCenter.getString("Image") + " 2:");
            l_Image2.setIcon(null);
            l_Image2.setBorder(null);
            l_Image2.setForeground(Color.BLACK);
            l_Image2.setBounds(new Rectangle(10, 220, 100, 20));
            l_Image2.setVisible(true);
            tf_Image2.setText(url[1]);
            tf_Image2.setVisible(true);
            l_Image3.setText(LangCenter.getString("Image") + " 3:");
            l_Image3.setIcon(null);
            l_Image3.setBorder(null);
            l_Image3.setForeground(Color.BLACK);
            l_Image3.setBounds(new Rectangle(10, 240, 100, 20));
            l_Image3.setVisible(true);
            tf_Image3.setText(url[2]);
            tf_Image3.setVisible(true);
            query = Database.executed_query("SELECT Test_body " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short");
            tp_TestcaseTestBody.setBackground(Color.WHITE);
            tp_TestcaseTestBody.setEditable(true);
            scrollTestBody.setBounds(new Rectangle(10, 290, 570, 270));
            tp_TestcaseTestBody.setText(query);
            tp_TestcaseTestBody.setCaretPosition(0);
            chb_EnableOptionalTest.setVisible(false);
            query = Database.executed_query("SELECT Optional_test " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short");
            l_TestcaseOptionalTest.setVisible(true);
            tp_TestcaseOptionalTest.setEnabled(true);
            tp_TestcaseOptionalTest.setBackground(Color.WHITE);
            tp_TestcaseOptionalTest.setEditable(true);
            tp_TestcaseOptionalTest.setText(query);
            tp_TestcaseOptionalTest.setCaretPosition(0);
            scrollOptionalTest.setVisible(true);
            b_Edit.setVisible(false);
            l_Status.setVisible(true);
            String Status = "";
            int StatusID = Integer.parseInt(Database.executed_query("SELECT Status " + "FROM testcase " + "WHERE ID='" + selectID + "';", "short"));
            if (StatusID == 0) {
                Status = LangCenter.getString("Disabled");
            } else if (StatusID == 1) {
                Status = LangCenter.getString("Active");
            }
            l_Status.setText(LangCenter.getString("Status") + ": " + Status);
        }
    }
