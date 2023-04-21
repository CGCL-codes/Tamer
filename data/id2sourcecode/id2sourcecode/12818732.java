    public void showLicense() {
        JPanel panel = new JPanel();
        JTextArea licenseAgreement = null;
        try {
            InputStream is = ClassLoader.getSystemResource("COPYING").openStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while (is.available() > 0) {
                bos.write(is.read());
            }
            licenseAgreement = new JTextArea(new String(bos.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't find license agreement.. Exiting.");
            System.exit(0);
        }
        licenseAgreement.setEditable(false);
        licenseAgreement.setRows(20);
        JScrollPane licenseScrollPane = new JScrollPane(licenseAgreement, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(licenseScrollPane);
        JOptionPane.showMessageDialog(this, panel, "License", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
    }
