    public void actionPerformed(ActionEvent evt) {
        File f = null;
        String str = null;
        boolean openfile = false, dirmenu = false, icon_menu = false;
        Oldfb1 comp;
        if (((JButton) evt.getSource()) == back_button) {
            record_button.setText("Record Memo");
            try {
                record_button.setIcon(new ImageIcon(new URL("http://veerex.googlecode.com/svn/trunk/src/record_icon.jpg")));
                memo_button.setIcon(new ImageIcon(new URL("http://veerex.googlecode.com/svn/trunk/src/memo_icon.jpg")));
                memo_button.setText("List Memos");
            } catch (MalformedURLException e) {
            }
            if (child.parent_dir != null) str = new String(child.parent_dir); else str = new String("C:\\");
            f = new File(str);
        } else if (((JButton) evt.getSource()) == dir_button) {
            str = new String(child.current_dir);
            f = new File(str);
            if (dir_button.getBackground().equals(Color.lightGray)) {
                dir_button.setBackground(this.getBackground());
                dir_button.setText("Off");
                child.showdir = false;
            } else {
                dir_button.setBackground(Color.lightGray);
                dir_button.setText("On");
                child.showdir = true;
            }
        } else if (((JButton) evt.getSource()) == pdf_button) {
            str = new String(child.current_dir);
            f = new File(str);
            current_selected.setBackground(this.getBackground());
            current_selected = pdf_button;
            current_selected.setBackground(Color.lightGray);
            child.filetype = Oldfb1.TYPE_PDF;
        } else if (((JButton) evt.getSource()) == photo_button) {
            str = new String(child.current_dir);
            f = new File(str);
            current_selected.setBackground(this.getBackground());
            current_selected = photo_button;
            current_selected.setBackground(Color.lightGray);
            child.filetype = Oldfb1.TYPE_PHOTO;
        } else if (((JButton) evt.getSource()) == movie_button) {
            str = new String(child.current_dir);
            f = new File(str);
            current_selected.setBackground(this.getBackground());
            current_selected = movie_button;
            current_selected.setBackground(Color.lightGray);
            child.filetype = Oldfb1.TYPE_MOVIE;
        } else if (((JButton) evt.getSource()) == doc_button) {
            str = new String(child.current_dir);
            f = new File(str);
            current_selected.setBackground(this.getBackground());
            current_selected = doc_button;
            current_selected.setBackground(Color.lightGray);
            child.filetype = Oldfb1.TYPE_DOC;
        } else if (((JButton) evt.getSource()) == all_button) {
            str = new String(child.current_dir);
            f = new File(str);
            current_selected.setBackground(this.getBackground());
            current_selected = all_button;
            current_selected.setBackground(Color.lightGray);
            child.filetype = Oldfb1.TYPE_ALL;
        } else if (((JButton) evt.getSource()) == memo_button) {
            if (((JButton) evt.getSource()).getText().equals("Play Memos")) {
                record_button.setText("Go!");
                record_button.setEnabled(true);
                memo_button.setText("Stop Memo");
                try {
                    record_button.setIcon(new ImageIcon(new URL("http://veerex.googlecode.com/svn/trunk/src/go_icon.jpg")));
                    beginPlayMemos();
                } catch (MalformedURLException e) {
                }
                return;
            } else if (((JButton) evt.getSource()).getText().equals("Stop Memo")) {
                memo_thread = null;
                record_button.setText("Record Memo");
                try {
                    record_button.setIcon(new ImageIcon(new URL("http://veerex.googlecode.com/svn/trunk/src/record_icon.jpg")));
                    memo_button.setIcon(new ImageIcon(new URL("http://veerex.googlecode.com/svn/trunk/src/memo_icon.jpg")));
                    memo_button.setText("List Memos");
                } catch (MalformedURLException e) {
                }
                return;
            } else if (((JButton) evt.getSource()).getText().equals("List Memos")) {
                str = new String("C://Veerex-AudioMarkers");
                f = new File(str);
                memo_button.setText("Play Memos");
                record_button.setText("Delete Memo");
                record_button.setEnabled(true);
                try {
                    record_button.setIcon(new ImageIcon(new URL("http://veerex.googlecode.com/svn/trunk/src/delete_icon.jpg")));
                    memo_button.setIcon(new ImageIcon(new URL("http://veerex.googlecode.com/svn/trunk/src/play_icon.jpg")));
                } catch (MalformedURLException e) {
                }
                child.filetype = Oldfb1.TYPE_ALL;
            }
        } else if (((JButton) evt.getSource()).getText().equals("Record Memo")) {
            lineOpen = true;
            beginRecording();
            record_button.setText("Stop");
            return;
        } else if (((JButton) evt.getSource()).getText().equals("Delete Memo")) {
            if (child.last_clicked_file != null) {
                str = new String("C://Veerex-AudioMarkers//" + child.last_clicked_file.getText());
                f = new File(str);
                System.out.println("file " + str + "deleted ?" + f.delete());
            }
            str = new String("C://Veerex-AudioMarkers");
            f = new File(str);
            current_selected.setBackground(this.getBackground());
            current_selected = memo_button;
            current_selected.setBackground(Color.lightGray);
            memo_button.setText("Play Memos");
            record_button.setText("Delete Memo");
            record_button.setEnabled(true);
            try {
                record_button.setIcon(new ImageIcon(new URL("http://veerex.googlecode.com/svn/trunk/src/delete_icon.jpg")));
                memo_button.setIcon(new ImageIcon(new URL("http://veerex.googlecode.com/svn/trunk/src/play_icon.jpg")));
            } catch (MalformedURLException e) {
            }
            child.filetype = Oldfb1.TYPE_ALL;
        } else if (((JButton) evt.getSource()).getText().equals("Stop")) {
            str = null;
            if (lineOpen) {
                lineOpen = false;
                thread = null;
                beginPlay(true);
                memo_button.setEnabled(true);
                record_button.setText("Record Memo");
                try {
                    record_button.setIcon(new ImageIcon(new URL("http://veerex.googlecode.com/svn/trunk/src/record_icon.jpg")));
                } catch (MalformedURLException e) {
                }
                return;
            }
        } else if (((JButton) evt.getSource()).getText().equals("Go!")) {
            openfile = true;
            icon_menu = true;
            Thread mthread = memo_thread;
            memo_thread = null;
            try {
                mthread.join();
            } catch (Exception e) {
            }
            str = current_memo.replace('`', '\\').replace('^', ':');
            str = str.substring(0, str.lastIndexOf(".wav"));
            record_button.setText("Record Memo");
            try {
                record_button.setIcon(new ImageIcon(new URL("http://veerex.googlecode.com/svn/trunk/src/record_icon.jpg")));
                memo_button.setText("List Memos");
                memo_button.setIcon(new ImageIcon(new URL("http://veerex.googlecode.com/svn/trunk/src/memo_icon.jpg")));
            } catch (MalformedURLException e) {
            }
        } else {
            openfile = true;
            if (type == 3) {
                str = new String(current_dir + "\\" + ((JButton) evt.getSource()).getText());
            } else if (type == 2) {
                sibling.record_button.setText("Record Memo");
                try {
                    sibling.record_button.setIcon(new ImageIcon(new URL("http://veerex.googlecode.com/svn/trunk/src/record_icon.jpg")));
                    sibling.memo_button.setIcon(new ImageIcon(new URL("http://veerex.googlecode.com/svn/trunk/src/memo_icon.jpg")));
                    sibling.memo_button.setText("List Memos");
                } catch (MalformedURLException e) {
                }
                str = new String(((JButton) evt.getSource()).getText());
                int j;
                for (j = 0; j < 1024; j++) {
                    if (buttons[j] == ((JButton) evt.getSource())) break;
                }
                if (j < 1024) {
                    System.out.println("j=" + j);
                    System.out.println("drive_index[j]" + drive_index[j]);
                    System.out.println("drive letter" + drives[drive_index[j]]);
                    str = new String(drives[drive_index[j]] + "\\" + ((JButton) evt.getSource()).getText());
                    System.out.println(str);
                }
                dirmenu = true;
            }
        }
        if (openfile) {
            System.out.println("action current" + str);
            f = new File(str);
        }
        if (f.isDirectory()) {
            if (child != null) {
                if (dirmenu) {
                    if (last_clicked_file != null) {
                        last_clicked_file.setContentAreaFilled(false);
                        last_clicked_file.setBackground(this.getBackground());
                    }
                    last_clicked_file = ((JButton) evt.getSource());
                    last_clicked_file.setBackground(Color.ORANGE);
                    last_clicked_file.setContentAreaFilled(true);
                }
                comp = child;
            } else {
                if (((JButton) evt.getSource() != last_clicked_file)) {
                    if (last_clicked_file != null) {
                        last_clicked_file.setBackground(this.getBackground());
                        last_clicked_file.setContentAreaFilled(false);
                    }
                    last_clicked_file = ((JButton) evt.getSource());
                    last_clicked_file.setBackground(Color.ORANGE);
                    last_clicked_file.setContentAreaFilled(true);
                    return;
                }
                last_clicked_file.setBackground(Color.ORANGE);
                comp = this;
            }
            comp.last_clicked_file = null;
            comp.removeAll();
            comp.validate();
            comp.pane.revalidate();
            comp.pane.repaint();
            comp.repaint();
            comp.listfiles(str);
            comp.repaint();
        } else {
            if (!icon_menu) {
                if (((JButton) evt.getSource() != last_clicked_file)) {
                    if (last_clicked_file != null) {
                        last_clicked_file.setBackground(this.getBackground());
                        last_clicked_file.setContentAreaFilled(false);
                    }
                    last_clicked_file = ((JButton) evt.getSource());
                    last_clicked_file.setBackground(Color.ORANGE);
                    last_clicked_file.setContentAreaFilled(true);
                    return;
                }
                last_clicked_file.setBackground(Color.ORANGE);
            }
            try {
                Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + str);
            } catch (Exception e) {
            }
        }
    }
