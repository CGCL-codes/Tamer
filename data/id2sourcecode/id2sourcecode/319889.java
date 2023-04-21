    @Override
    public void actionPerformed(ActionEvent e) {
        if (txtUrl.getText().equals("") || !txtUrl.getText().contains("youtube.com/watch?v=")) {
            showError("Insert a Valid Youtube Video Link");
            txtUrl.requestFocus();
            return;
        }
        if (txtSave.getText().equals("")) {
            txtSave.setText(appDir + File.separator + "Download");
            if (!new File(appDir + File.separator + "Download").exists()) new File(appDir + File.separator + "Download").mkdir();
        }
        video = new Youtube(txtUrl.getText(), (String) cmbSource.getSelectedItem());
        try {
            filename = txtSave.getText() + File.separator + video.getVideoTitle() + video.getExt();
            jProgressBar.setString("Retrieving Video Details");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        if (new File(filename).exists()) {
            int response;
            response = JOptionPane.showConfirmDialog(null, filename + " already exists. Do you want to overwrite it?");
            if (response == 1 || response == 2) return;
        }
        cmdExit.setEnabled(true);
        task = new Task();
        jProgressBar.setVisible(true);
        task.addPropertyChangeListener(this);
        task.execute();
    }
