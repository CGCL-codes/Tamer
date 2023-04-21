    private void save() {
        if (last_recorded_audio == null) {
            int end = JOptionPane.showConfirmDialog(null, "No recording has been made.\nDo you wish to make a recording?", "WARNING", JOptionPane.YES_NO_OPTION);
            if (end == JOptionPane.NO_OPTION) cancel();
        } else {
            stopRecording();
            stopPlayback();
            if (save_file_chooser == null) {
                save_file_chooser = new JFileChooser();
                save_file_chooser.setCurrentDirectory(new File("."));
                save_file_chooser.setFileFilter(new FileFilterAudio());
            }
            int dialog_result = save_file_chooser.showSaveDialog(RecordingFrame.this);
            if (dialog_result == JFileChooser.APPROVE_OPTION) {
                File save_file = save_file_chooser.getSelectedFile();
                boolean proceed = true;
                String correct_format_name = (String) choose_file_format_combo_box.getSelectedItem();
                AudioFileFormat.Type correct_format = AudioMethods.getAudioFileFormatType(correct_format_name);
                save_file = ensureCorrectExtension(save_file, correct_format);
                if (save_file.exists()) {
                    int overwrite = JOptionPane.showConfirmDialog(null, "This file already exists.\nDo you wish to overwrite it?", "WARNING", JOptionPane.YES_NO_OPTION);
                    if (overwrite != JOptionPane.YES_OPTION) proceed = false;
                }
                if (proceed) {
                    try {
                        AudioMethods.saveToFile(last_recorded_audio, save_file, correct_format);
                        File[] to_add_to_table = new File[1];
                        to_add_to_table[0] = save_file;
                        controller.addRecordingsAction.addRecording(to_add_to_table);
                        cancel();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
