        public void actionPerformed(ActionEvent e) {
            if (!(filename.equalsIgnoreCase("Untitled.java"))) {
                debug.setText("Preparing Source File...........\n.");
                String sendfilename = new String("Temp.java");
                try {
                    DataOutputStream d = new DataOutputStream(new FileOutputStream(sendfilename));
                    String line = editor.getText();
                    BufferedReader br = new BufferedReader(new StringReader(line));
                    while ((line = br.readLine()) != null) d.writeBytes(line + "\r\n");
                    d.close();
                } catch (Exception ex) {
                    System.out.println("File not found");
                }
                editor.requestFocus();
                debug.append("Source File Prepared Successfully...........\n.");
                try {
                    debug.append("\nPreparing to send Source File send to : Host ");
                    send(sendfilename);
                } catch (IOException ioe) {
                    System.out.println("File saving error");
                }
            } else {
                JOptionPane.showMessageDialog(Rce.this, "First Edit The source File", "Source Edit", JOptionPane.INFORMATION_MESSAGE);
            }
        }
