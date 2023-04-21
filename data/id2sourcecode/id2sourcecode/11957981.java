    void update(ExtensionInfo extensionInfo) {
        author.setText(getLabelText(extensionInfo.getAuthor()));
        version.setText(getLabelText(extensionInfo.getVersion()));
        email.setText(getLabelText(extensionInfo.getEmail()));
        homePage.setText(getLabelText(extensionInfo.getHomePage()));
        description.setText(getLabelText(ApplicationContext.getInstance().getI18NBundleValue(extensionInfo.getName(), extensionInfo.getDescriptionBundleKey())));
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE) || homePage.getText().equals(notProvidedTxt)) homePage.setEnabled(false);
        if (email.getText().equals(notProvidedTxt)) email.setEnabled(false);
    }
