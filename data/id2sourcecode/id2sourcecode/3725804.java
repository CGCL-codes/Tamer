    private void downloadFile() {
        try {
            URLConnection con = url.openConnection();
            BufferedInputStream in = new BufferedInputStream(con.getInputStream());
            FileOutputStream out = new FileOutputStream("pdb" + pdbCode + ".ent.gz");
            downloaded = 0;
            int i = 0;
            byte[] buffer = new byte[1024];
            while ((i = in.read(buffer)) >= 0) {
                out.write(buffer, 0, i);
                downloaded += i;
            }
            out.close();
            in.close();
        } catch (UnknownHostException e) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    JOptionPane.showMessageDialog(null, "The PDB file couldn't be retrieved from the server! \n" + "Please check your connection to server...", "Unknown host error", JOptionPane.ERROR_MESSAGE);
                }
            });
        } catch (Exception ex) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    JOptionPane.showMessageDialog(null, "The PDB file couldn't be downloaded.! \n" + "Please use a valid 4-digit-code for an existing PDB file.", "Download file error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }
