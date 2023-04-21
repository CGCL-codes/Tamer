    private boolean makeNewNcFile() {
        if (txt_dest_file.getText().isEmpty()) {
            NCMsgBox msg = new NCMsgBox(frame, "Error", "Select files", 150, 150, 150, 100);
            return false;
        }
        new_nc_file_name = txt_dest_file.getText();
        try {
            if (ncwriter.isRunning() || ncscale.isRunning()) {
                NCMsgBox msg = new NCMsgBox(frame, "Error", "NC Writer or NC Scale is already running", 150, 150, 150, 100);
                return false;
            }
            ncwriter.setNC(new_nc_file_name, nc_file_name, profiles, progress_bar);
            progress_bar.setValue(0);
            Thread writer_t = new Thread(ncwriter);
            writer_t.start();
            System.out.println(new_nc_file_name + "  " + nc_file_name);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
