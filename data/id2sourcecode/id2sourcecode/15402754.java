    public void run() {
        int resultValue = chatApp.getJfm().showSaveDialog(chatApp.getChatClientFrame());
        if (resultValue == JFileChooser.APPROVE_OPTION) {
            File temp = chatApp.getJfm().getSelectedFile();
            if (temp.exists()) {
                int n = JOptionPane.showConfirmDialog(chatApp.getChatClientFrame(), "File Already Exist!, Overwrite it?", "Warning", JOptionPane.OK_CANCEL_OPTION);
                if (n != JOptionPane.OK_OPTION) {
                    parentPanel.addTextChat("File transfer cancelled!");
                    TextMessage tm = new TextMessage();
                    tm.setMessage("File [" + bm.getDesc() + "] transfered Cancelled!");
                    tm.setToUserId(bm.getFromUserId());
                    tm.setTargetType(IConstant.USER_CHAT);
                    chatApp.sendTextMessage(tm);
                    return;
                }
            }
            if (bm.getData() == null) {
                parentPanel.addTextChat("File is empty!");
                TextMessage tm = new TextMessage();
                tm.setMessage("File [" + bm.getDesc() + "] transfered with no data!");
                tm.setToUserId(bm.getFromUserId());
                tm.setTargetType(IConstant.USER_CHAT);
                chatApp.sendTextMessage(tm);
                return;
            }
            FileOutputStream fos = null;
            try {
                if (chatApp.getSecurityMode() == 2) {
                    FileEncrypter.getInstance().fileDecrypt(bm.getData(), temp);
                } else {
                    temp.createNewFile();
                    fos = new FileOutputStream(temp);
                    fos.write(bm.getData());
                }
                parentPanel.addTextChat("File [" + bm.getDesc() + "] transfered successfully! with the name [" + temp.getName() + "]");
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (fos != null) fos.close();
                } catch (Exception e) {
                }
            }
            TextMessage tm = new TextMessage();
            tm.setMessage("File [" + bm.getDesc() + "] transfered successfully!");
            tm.setToUserId(bm.getFromUserId());
            tm.setTargetType(IConstant.USER_CHAT);
            chatApp.sendTextMessage(tm);
        } else {
            parentPanel.addTextChat("File transfer cancelled!");
            TextMessage tm = new TextMessage();
            tm.setMessage("File [" + bm.getDesc() + "] transfered Cancelled!");
            tm.setToUserId(bm.getFromUserId());
            tm.setTargetType(IConstant.USER_CHAT);
            chatApp.sendTextMessage(tm);
        }
    }
