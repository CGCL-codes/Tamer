    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Synchroniser")) {
            String text = "";
            int sync_duree = Integer.parseInt(vue.duree_area.getText());
            String action_string = (String) combo.getSelectedItem();
            if (action_string.equals("Augmenter")) {
                augmenter = true;
            } else {
                augmenter = false;
            }
            try {
                String[] temp;
                String delimiter = " --> ";
                Calendar calendar_debut = Calendar.getInstance();
                Calendar calendar_fin = Calendar.getInstance();
                Date date_debut;
                Date date_fin;
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss,SSS");
                String line = null;
                BufferedReader bufferReader;
                String str_timeline = "";
                boolean line_is_int = false;
                int k = 0;
                bufferReader = new BufferedReader(new StringReader(vue.editorPane.getText()));
                while ((line = bufferReader.readLine()) != null) {
                    if (line_is_int) {
                        try {
                            temp = line.split(delimiter);
                            date_debut = formatter.parse(temp[0]);
                            calendar_debut.setTime(date_debut);
                            date_fin = formatter.parse(temp[1]);
                            calendar_fin.setTime(date_fin);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        if (augmenter) {
                            calendar_debut.set(Calendar.SECOND, calendar_debut.get(Calendar.SECOND) + sync_duree);
                            date_debut = calendar_debut.getTime();
                            text += formatter.format(date_debut);
                            vue.table.setValueAt(formatter.format(date_debut), k, 1);
                            calendar_fin.set(Calendar.SECOND, calendar_fin.get(Calendar.SECOND) + sync_duree);
                            date_fin = calendar_fin.getTime();
                            text += " --> " + formatter.format(date_fin) + "\n";
                            vue.table.setValueAt(formatter.format(date_fin), k, 2);
                            k++;
                        } else {
                            calendar_debut.set(Calendar.SECOND, calendar_debut.get(Calendar.SECOND) - sync_duree);
                            date_debut = calendar_debut.getTime();
                            text += formatter.format(date_debut);
                            vue.table.setValueAt(formatter.format(date_debut), k, 1);
                            calendar_fin.set(Calendar.SECOND, calendar_fin.get(Calendar.SECOND) - sync_duree);
                            date_fin = calendar_fin.getTime();
                            text += " --> " + formatter.format(date_fin) + "\n";
                            vue.table.setValueAt(formatter.format(date_fin), k, 2);
                            k++;
                        }
                        str_timeline += line + "\n";
                        line_is_int = false;
                    } else {
                        text += line + "\n";
                    }
                    char[] all = line.toCharArray();
                    for (int i = 0; i < all.length; i++) {
                        if (Character.isDigit(all[i])) {
                            line_is_int = true;
                        } else {
                            line_is_int = false;
                            break;
                        }
                    }
                }
                Lanceur.sub.updateEditor();
                Lanceur.sub.updatePane();
                vue.getJEditorPane().setText(text);
                vue.getJEditorPane().setCaretPosition(0);
            } catch (Exception ex) {
            }
        }
    }
