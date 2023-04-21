    protected void okPressed() {
        if (userIdText.getText().equals("")) {
            MessageDialog.openError(getShell(), "iWallet - ��¼", "�����û�����Ϊ�ա�");
            return;
        }
        if (serverText.getText().equals("")) {
            MessageDialog.openError(getShell(), "iWallet - ��¼", "���󣺷���������Ϊ�ա�");
            return;
        }
        if (GuiParameter.requester == null) {
            GuiParameter.client = new NetworkTransportClient(serverText.getText());
            try {
                GuiParameter.client.connect();
                GuiParameter.requester = new ServiceRequester(GuiParameter.client);
            } catch (TransportException e2) {
                MessageDialog.openError(getShell(), "iWallet - ��¼", "��¼ʧ�ܣ��޷����ӵ���������");
                return;
            }
        }
        try {
            GuiParameter.logined = GuiParameter.requester.login(userIdText.getText(), passwordText.getText());
        } catch (ServerCommunicationException e) {
            MessageDialog.openError(getShell(), "iWallet - ��¼", "��¼ʧ�ܣ��������ͨѶ�������");
            return;
        }
        if (GuiParameter.logined) {
            connectionDetails = new ConnectionDetails(userIdText.getText(), serverText.getText(), passwordText.getText());
            savedDetails.put(userIdText.getText(), connectionDetails);
            saveDescriptors();
            super.okPressed();
        } else {
            MessageDialog.openError(getShell(), "iWallet - ��¼", "��¼ʧ�ܣ��û������ڻ��������");
        }
    }
