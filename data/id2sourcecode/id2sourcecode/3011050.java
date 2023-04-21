    public void saveChar(Character ch_data) {
        String s_path = "";
        try {
            s_path = "sheets/" + s_char_name + ".gcs";
            File f_new = new File(s_path);
            if (f_new.createNewFile()) {
                save(ch_data, s_path);
            } else {
                int i_choice = JOptionPane.showConfirmDialog(null, "A character sheet for this character already exists." + "  Do you want to overwrite it?");
                if (i_choice == JOptionPane.CANCEL_OPTION || i_choice == JOptionPane.NO_OPTION) {
                    return;
                } else {
                    save(ch_data, s_path);
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
