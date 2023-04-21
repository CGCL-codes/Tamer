    public JButton getFetchCertificateButton() {
        if (fetchCertButton == null) {
            fetchCertButton = new JButton(UIHelper.getText("investigatecard.opencert"));
            fetchCertButton.setBounds(ToLiMaGUI.getNextButtonPos());
            fetchCertButton.setIcon(UIHelper.getImage("cert_view.gif"));
            fetchCertButton.setVisible(CommonUtils.isDesktopSupported());
        }
        return fetchCertButton;
    }
