    private void initComponents() {
        setBorder(new LineBorder(Color.black, 1, false));
        setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("30px"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("120px"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("49dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("230px"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("120px"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("77px"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("66dlu") }, new RowSpec[] { FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("30dlu"), FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC }));
        checkBox = new JCheckBox();
        checkBox.setVisible(edit);
        add(checkBox, new CellConstraints(1, 1, 1, 13));
        final JLabel logoLabel = new JLabel();
        logoLabel.setIcon(UIHelper.getImage(AdminInterfacesFactory.getCALogoSelector().getLogo(currentCAStatusAnalyser.getCAInfoVO())));
        add(logoLabel, new CellConstraints(3, 3));
        final JLabel cANameLabel = new JLabel();
        cANameLabel.setText(currentCAStatusAnalyser.getCAInfoVO().getCAName());
        cANameLabel.setFont(UIHelper.getFont("Label.font.bold"));
        add(cANameLabel, new CellConstraints(5, 3, 7, 1));
        final JLabel subjectLabel = new JLabel();
        subjectLabel.setText(UIHelper.getText("adminca.subject") + ":");
        add(subjectLabel, new CellConstraints(3, 4));
        final JLabel issuerLabel = new JLabel();
        issuerLabel.setText(UIHelper.getText("adminca.issuer") + ":");
        add(issuerLabel, new CellConstraints(3, 5));
        final JLabel caIdLabel = new JLabel();
        caIdLabel.setText(UIHelper.getText("adminca.caid") + ":");
        add(caIdLabel, new CellConstraints(3, 7));
        final JLabel subjectField = new JLabel();
        subjectField.setText(currentCAStatusAnalyser.getSubject());
        add(subjectField, new CellConstraints(5, 4, 9, 1));
        final JLabel issuerField = new JLabel();
        issuerField.setText(currentCAStatusAnalyser.getIssuer());
        add(issuerField, new CellConstraints(5, 5, 9, 1));
        final JLabel caIdField = new JLabel();
        caIdField.setText("" + currentCAStatusAnalyser.getCAInfoVO().getCAId());
        add(caIdField, new CellConstraints(5, 7, 3, 1));
        final JLabel cAStatuslabel = new JLabel();
        cAStatuslabel.setText(UIHelper.getText("adminstatus.status") + ":");
        add(cAStatuslabel, new CellConstraints(3, 8));
        cAStatusField = new JLabel();
        cAStatusField.setText(AdminUIUtils.getCAStatusTranslated(currentCAStatusAnalyser.getCAInfoVO().getCAStatus()));
        add(cAStatusField, new CellConstraints(5, 8, 3, 1));
        final JLabel cATokenStatuslabel = new JLabel();
        cATokenStatuslabel.setText(UIHelper.getText("adminstatus.catokenstatus") + ":");
        add(cATokenStatuslabel, new CellConstraints(3, 10));
        final CATokenStatusTablePanel tokenStatusTablePanel = new CATokenStatusTablePanel(cAId);
        add(tokenStatusTablePanel, new CellConstraints(3, 11, 9, 1));
        final JLabel validFromLabel = new JLabel();
        validFromLabel.setText(UIHelper.getText("adminca.validfrom") + ":");
        add(validFromLabel, new CellConstraints(9, 7));
        final JLabel validToLabel = new JLabel();
        validToLabel.setText(UIHelper.getText("adminca.validto") + ":");
        add(validToLabel, new CellConstraints(9, 8));
        final JButton viewCACertButton = new JButton();
        viewCACertButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                OpenOrSaveCertDialog d = new OpenOrSaveCertDialog(currentCAStatusAnalyser.getCACertificate(), (Window) SwingUtilities.getRoot(thisPanel));
                d.setLocationRelativeTo(SwingUtilities.getRootPane(thisPanel));
                d.setVisible(true);
            }
        });
        viewCACertButton.setIcon(UIHelper.getImage("cert_view.gif"));
        viewCACertButton.setText(UIHelper.getText("investigatecard.viewcert"));
        viewCACertButton.setVisible(CommonUtils.isDesktopSupported());
        viewCACertButton.setEnabled(currentCAStatusAnalyser.getCACertificate() != null);
        add(viewCACertButton, new CellConstraints(7, 13));
        final JLabel validFromField = new JLabel();
        validFromField.setText(currentCAStatusAnalyser.getCACertificate().getNotBefore().toString());
        add(validFromField, new CellConstraints(11, 7, 3, 1));
        final JLabel validToField = new JLabel();
        validToField.setText(currentCAStatusAnalyser.getCACertificate().getNotAfter().toString());
        add(validToField, new CellConstraints(11, 8, 3, 1));
        final JButton getCRLButton = new JButton();
        getCRLButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                try {
                    X509CRL crl = AdminInterfacesFactory.getCAManager().getLastCRL(currentCAStatusAnalyser.getCAInfoVO().getCAId());
                    if (crl == null) {
                        displayEmtpyCRLWarning();
                    } else {
                        OpenOrSaveCRLDialog d = new OpenOrSaveCRLDialog(crl, (Window) SwingUtilities.getRoot(thisPanel));
                        d.setLocationRelativeTo(SwingUtilities.getRootPane(thisPanel));
                        d.setVisible(true);
                    }
                } catch (IOException e1) {
                    LocalLog.getLogger().log(Level.SEVERE, "Error occured when fetching CRL : " + e1.getMessage(), e1);
                    AdminUIUtils.showErrorMsg("adminca.errorfetchingcrl", thisPanel);
                } catch (AuthorizationDeniedException_Exception e1) {
                    LocalLog.getLogger().log(Level.SEVERE, "Error occured when fetching CRL : " + e1.getMessage(), e1);
                    AdminUIUtils.showErrorMsg("adminca.errorfetchingcrl", thisPanel);
                }
            }
        });
        getCRLButton.setIcon(UIHelper.getImage("crl.gif"));
        getCRLButton.setText(UIHelper.getText("adminca.fetchcrl"));
        add(getCRLButton, new CellConstraints(9, 13, 3, 1, CellConstraints.FILL, CellConstraints.FILL));
    }
