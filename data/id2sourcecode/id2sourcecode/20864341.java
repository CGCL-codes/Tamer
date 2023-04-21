    private void createSpreadSheet() {
        TranslatorWriter writer = new TranslatorWriter();
        try {
            if (studio) {
                writer.write(propertiesFile, spreadSheetFile);
            } else {
                writer.write(propertiesFileE, spreadSheetFileE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
