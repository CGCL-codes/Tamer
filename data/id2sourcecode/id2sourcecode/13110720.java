    public static void applyPatch(View view) {
        try {
            PatchSelectionDialog dialog = new PatchSelectionDialog(view);
            DualDiffUtil.center(view, dialog);
            dialog.setVisible(true);
            String patch_file = dialog.getPatchFile();
            if (patch_file == null || patch_file.length() == 0) {
                return;
            }
            Reader reader = new BufferedReader(new FileReader(patch_file));
            StringWriter writer = new StringWriter();
            PatchUtils.copyToWriter(reader, writer);
            String patch = writer.toString();
            if (patch == null || patch.length() == 0) {
                JOptionPane.showMessageDialog(view, "Invalid patch file, file has no content.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Buffer buffer = view.getEditPane().getBuffer();
            String bufferText = buffer.getText(0, buffer.getLength());
            String results = Patch.patch(patch, bufferText);
            jEdit.newFile(view).insert(0, results);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
