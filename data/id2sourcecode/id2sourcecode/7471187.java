    public static boolean showEula() {
        try {
            URL url = ClassLoader.getSystemResource(EULA_FILE);
            if (url == null) {
                Log.error(ERROR_EULA_FILE.getText() + EULA_FILE);
                return false;
            }
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            StringBuffer eulaText = new StringBuffer(1000);
            int ch;
            while ((ch = isr.read()) != -1) eulaText.append((char) ch);
            isr.close();
            is.close();
            String optionButtons[] = { TEXT_EULA_ACCEPT.getText(), TEXT_EULA_DECLINE.getText() };
            JTextArea textArea = new JTextArea(eulaText.toString());
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            int rv = JOptionPane.showOptionDialog(null, scrollPane, TEXT_EULA_TITLE.getText(), JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, optionButtons, optionButtons[0]);
            return (rv == 0);
        } catch (Exception e) {
            Log.error("MdnLicenceManager.showEula: ", e);
            return false;
        }
    }
