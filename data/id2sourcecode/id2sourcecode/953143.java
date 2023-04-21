    public void menuAction(String name) {
        if (name.equals("Open")) {
            JFileChooser jfc = new JFileChooser("c:\\");
            jfc.showOpenDialog(null);
            File file = jfc.getSelectedFile();
            Scanner sc = null;
            try {
                sc = new Scanner(file);
            } catch (FileNotFoundException ex) {
            }
            clear();
            while (sc.hasNextLine()) {
                println(sc.nextLine());
            }
        } else if (name.equals("Save")) {
            JFileChooser jfc = new JFileChooser("c:\\");
            jfc.showSaveDialog(null);
            File file = jfc.getSelectedFile();
            PrintStream ps = null;
            try {
                ps = new PrintStream(file);
            } catch (FileNotFoundException ex1) {
            }
            String s = console.getText();
            ps.print(s);
            ps.close();
            JOptionPane.showMessageDialog(null, "Output has been saved as\n" + file.getPath(), "File Saved", JOptionPane.DEFAULT_OPTION);
        } else if (name.equals("Close")) {
            System.exit(0);
        } else if (name.equals("Copy Selection")) {
            setClipboard(console.getSelectedText());
        } else if (name.equals("Copy Input")) {
            setClipboard(sysIn.getText());
        } else if (name.equals("Paste to Input")) {
            sysIn.setText(getClipboard());
        } else if (name.equals("Paste to Console")) {
            println(getClipboard());
        } else if (name.equals("Select All")) {
            console.setSelectionStart(0);
            console.setSelectionEnd(console.getText().length());
        } else if (name.equals("Toggle Edit Lock")) {
            editLock = !editLock;
            setEditLock(editLock);
        } else if (name.equals("Help Me!")) {
            JOptionPane.showMessageDialog(null, "Coming Soon!", "Help", JOptionPane.DEFAULT_OPTION);
        } else if (name.equals("About SC++")) {
            JOptionPane.showMessageDialog(null, "SenezConsole++\nVersion 1.0\n\nby Chris Senez", "SC++", JOptionPane.DEFAULT_OPTION);
        } else if (name.equals("Contact Senez")) {
            JOptionPane.showMessageDialog(null, "email: csenez64@yahoo.com", "Contact", JOptionPane.DEFAULT_OPTION);
        } else if (name.equals("Submit a Bug")) {
            JOptionPane.showMessageDialog(null, "email: csenez64@yahoo.com\nsubject: sc++ bug report\nmessage: details", "Bugfix", JOptionPane.DEFAULT_OPTION);
        }
    }
