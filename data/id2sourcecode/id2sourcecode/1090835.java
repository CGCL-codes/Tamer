    private void compileAndLoad(File f) {
        try {
            ByteArrayOutputStream code = new ByteArrayOutputStream();
            FileInputStream in = new FileInputStream(f);
            String result = parser.parsingFile(f.toString(), in, code);
            in.close();
            if (result != null) {
                JOptionPane.showMessageDialog(null, result, "Info", JOptionPane.ERROR_MESSAGE);
                return;
            }
            load(new ByteArrayInputStream(code.toByteArray()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Exception", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
