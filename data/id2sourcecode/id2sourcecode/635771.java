        public void actionPerformed(ActionEvent e) {
            FileDialog fd = new FileDialog(Rce.this, "Select File", FileDialog.SAVE);
            fd.show();
            if (fd.getFile() != null) {
                dirname = fd.getDirectory();
                filename = fd.getDirectory() + fd.getFile();
                setTitle("Remote compiler -- " + filename);
                try {
                    DataOutputStream d = new DataOutputStream(new FileOutputStream(filename));
                    String line = editor.getText();
                    BufferedReader br = new BufferedReader(new StringReader(line));
                    while ((line = br.readLine()) != null) d.writeBytes(line + "\r\n");
                    d.close();
                } catch (Exception ex) {
                    System.out.println("File not found");
                }
                editor.requestFocus();
            }
        }
