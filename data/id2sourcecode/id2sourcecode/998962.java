    private void showBugs() {
        String msg = "If you find any bugs while usng this program, please help us\n by submitting any bugs by pressing the report button bellow";
        int Dialog = JOptionPane.showConfirmDialog(null, "Help us by submiting bug reports.\nPress yes to open a mail client to send us your bugs", "Bug Reports", JOptionPane.YES_NO_OPTION);
        if (Dialog == 0 && Desktop.isDesktopSupported()) {
            Desktop a = Desktop.getDesktop();
            try {
                URI email = new URI("mailto", "bug.lucerna@gmail.com", null);
                a.mail(email);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Is Desktop Supported: " + Desktop.isDesktopSupported());
                JOptionPane.showMessageDialog(null, "Error, please mail to bug.lucerna@gmail.com", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (Dialog == 0 && !Desktop.isDesktopSupported()) {
            JOptionPane.showMessageDialog(null, "Platform not supported", "Error", JOptionPane.ERROR_MESSAGE);
        } else System.out.println("Canceled");
    }
