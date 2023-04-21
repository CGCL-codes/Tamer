    private void cmd_file() {
        JFileChooser jfc = new JFileChooser();
        jfc.setMultiSelectionEnabled(false);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.showOpenDialog(this);
        File imageFile = jfc.getSelectedFile();
        if (imageFile == null || imageFile.isDirectory() || !imageFile.exists()) return;
        String fileName = imageFile.getAbsolutePath();
        byte[] data = null;
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 8];
            int length = -1;
            while ((length = fis.read(buffer)) != -1) os.write(buffer, 0, length);
            fis.close();
            data = os.toByteArray();
            os.close();
            ImageIcon image = new ImageIcon(data, fileName);
            imageLabel.setIcon(image);
        } catch (Exception e) {
            log.log(Level.WARNING, "load image", e);
            return;
        }
        fileButton.setText(imageFile.getAbsolutePath());
        pack();
        m_mImage.setName(fileName);
        m_mImage.setImageURL(fileName);
        m_mImage.setBinaryData(data);
    }
