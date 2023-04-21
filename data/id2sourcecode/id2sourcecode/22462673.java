    private void createInputStream(ActionEvent event, String url) {
        try {
            event.setSource((new URL(url)).openStream());
            externalActionListener.actionPerformed(event);
        } catch (Exception exception) {
            final String message = url + NEW_LINE + exception.getClass().getName() + NEW_LINE + exception.getLocalizedMessage();
            JOptionPane.showMessageDialog(parent, message, getString("Load_error_caption"), JOptionPane.ERROR_MESSAGE);
        }
    }
