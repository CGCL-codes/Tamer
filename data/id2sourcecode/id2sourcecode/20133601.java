    private void saveFile() {
        FileUtils.copyFile(filename, filename + ".bak");
        JdomWriter.setNextNodeWithTagValue(elements, "base-url", getHostNameText());
        JdomWriter.setNextNodeWithTagValue(elements, "locale-language", getLocaleLanguageText());
        JdomWriter.setNextNodeWithTagValue(elements, "locale-country", getLocaleCountryText());
        JdomWriter.setNextNodeWithTagValue(elements, "encoding", getEncodingText());
        JdomWriter.setNextNodeWithTagValue(elements, "solution-path", getSolutionPathText());
        JdomWriter.saveXML(filename, doc);
    }
