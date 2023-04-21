    private void onMenuExportPHP() {
        if (m_inputFile == null) {
            JOptionPane.showMessageDialog(this, "You need to open a QML file first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        m_fileChooser.setFileFilter(new FileNameExtensionFilter("PHP Files (*.php)", "php"));
        if (m_fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = m_fileChooser.getSelectedFile();
            if (file.exists()) {
                if (JOptionPane.showConfirmDialog(this, "The specified file already exists.\nDo you want to overwrite it?") == JOptionPane.CANCEL_OPTION) return;
                file.delete();
            }
            try {
                Writer out = new FileWriter(file);
                PHPRenderer renderer = new PHPRenderer(out, "");
                m_quest.render(renderer);
                out.flush();
                out.close();
                JOptionPane.showMessageDialog(this, "Output complete.  The quest will need all of the PHP engine files to run.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error rendering file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
