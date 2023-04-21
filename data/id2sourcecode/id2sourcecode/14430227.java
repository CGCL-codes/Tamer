    private void addFileToZip(ZipOutputStream zipStream, File file) throws Exception {
        String name = file.getAbsolutePath().replace('\\', '/');
        name = name.substring(m_fileSelector.getCurrentDirectory().getAbsolutePath().length());
        m_message = m_messageOutputAdding + " " + name + "..";
        m_step++;
        repaint();
        ZipEntry entry = new ZipEntry(name);
        zipStream.putNextEntry(entry);
        writeFileBytes(file, zipStream);
        zipStream.closeEntry();
    }
