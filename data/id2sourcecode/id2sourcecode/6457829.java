    public static boolean guiWriteHtmlFile(final String text, final Component p) throws IOException {
        if (fc.showSaveDialog(p) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(HTML_EXT)) {
                f = new File(f.getParent(), f.getName() + HTML_EXT);
            }
            if (!(f.exists() && !showWarning(p, "The file " + f.getName() + " already exists. " + "Are you sure you want to " + "overwrite it?"))) {
                final FileOutputStream fos = new FileOutputStream(f);
                fos.write(text.getBytes());
                return true;
            }
        }
        return false;
    }
