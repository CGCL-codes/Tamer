    private void saveFile() {
        FileUtils.copyFile(filename, filename + ".bak");
        JdomWriter.setNodeWithTagValue(elements, "publisher-password", getPasswordText());
        JdomWriter.saveXML(filename, doc);
    }
