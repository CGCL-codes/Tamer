    public void setValueAt(Object value, int row, int col) {
        int i;
        for (i = 0; i < books.size(); i++) if (books.get(i).getName().equals(display.get(row).getName())) break;
        if (col == 0) {
            modified = modified || !(books.get(row).getName().equals((String) value));
            books.get(i).setName((String) value);
            display.get(row).setName((String) value);
        } else if (col == 1) {
            modified = modified || !(books.get(row).getAuthor().equals((String) value));
            books.get(i).setAuthor((String) value);
            display.get(row).setAuthor((String) value);
            if (((String) value) != "" && !authors.contains((String) value)) {
                authors.add((String) value);
                Collections.sort(authors);
            }
        } else if (col == 2) {
            if (Book.valid((String) value)) {
                modified = modified || !(Book.toText(books.get(row).getList()).equals((String) value));
                books.get(i).setList(Book.toList((String) value));
                display.get(row).setList(Book.toList((String) value));
            }
        } else if (col == 3) {
            modified = modified || !(books.get(row).getRead().equals((Boolean) value));
            books.get(i).setRead((Boolean) value);
            display.get(row).setRead((Boolean) value);
        } else if (col == 4) {
            modified = modified || !(books.get(row).getEnd().equals((Boolean) value));
            books.get(i).setEnd((Boolean) value);
            display.get(row).setEnd((Boolean) value);
        } else if (col == 5) {
            modified = modified || !(books.get(row).getBurn().equals((Boolean) value));
            books.get(i).setBurn((Boolean) value);
            display.get(row).setBurn((Boolean) value);
        } else if (col == 6) {
            modified = modified || !(new SimpleDateFormat("yyyy.MM.dd.mm.ss").format(books.get(row).getDate()).equals((String) value));
            try {
                books.get(i).setDate(new SimpleDateFormat("yyyy.MM.dd.mm.ss").parse((String) value));
                display.get(row).setDate(new SimpleDateFormat("yyyy.MM.dd.mm.ss").parse((String) value));
            } catch (Exception e) {
            }
        } else if (col == 7) {
            modified = modified || !(books.get(row).getNotes().equals((String) value));
            books.get(i).setNotes((String) value);
            display.get(row).setNotes((String) value);
        }
        if (modified) {
            if (col != 6) {
                books.get(i).setDate(new Date(System.currentTimeMillis()));
                display.get(row).setDate(new Date(System.currentTimeMillis()));
                fireTableCellUpdated(row, 6);
            }
            fireTableCellUpdated(row, col);
        }
    }
