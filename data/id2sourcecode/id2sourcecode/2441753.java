    private void browseXmlFile() {
        fileChooser.setFileFilter(new XmlFileFilter());
        fileChooser.setCurrentDirectory(archiveBean.getClassLoc());
        int returnVal = fileChooser.showOpenDialog(butSelect);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File xmlfile = fileChooser.getSelectedFile();
            txtServiceXml.setText(xmlfile.getAbsolutePath());
            byte[] buf = new byte[1024];
            int read;
            ByteArrayOutputStream out;
            try {
                FileInputStream in = new FileInputStream(xmlfile);
                out = new ByteArrayOutputStream();
                while ((read = in.read(buf)) > 0) {
                    out.write(buf, 0, read);
                }
                in.close();
                value = new String(out.toByteArray());
            } catch (IOException e1) {
            }
        } else {
            txtServiceXml.setText("");
        }
    }
