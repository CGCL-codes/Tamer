    public void draw(boolean[] columns) {
        StringBuilder stringbuilder = new StringBuilder();
        String line = "";
        if (columns[0]) {
            stringbuilder.append(addSpaces("TIME", 10));
            line = line.concat("----------");
        }
        if (columns[1]) {
            stringbuilder.append(addSpaces("CHANNEL", 20));
            line = line.concat("--------------------");
        }
        if (columns[2]) {
            stringbuilder.append(addSpaces("DESCRIPTION", 45));
            line = line.concat("---------------------------------------------");
        }
        if (columns[3]) {
            stringbuilder.append(addSpaces("GENRE", 15));
            line = line.concat("---------------");
        }
        stringbuilder.append("\n");
        stringbuilder.append(line).append("\n");
        if (list.isEmpty()) {
            stringbuilder.append("No entries found, sorry.");
        } else {
            for (Event event : list) {
                if (columns[0]) {
                    stringbuilder.append(addSpaces(" " + getTimeFormat(event.getDate()), 10));
                }
                if (columns[1]) {
                    stringbuilder.append(addSpaces(" " + event.getChannel(), 20));
                }
                if (columns[2]) {
                    stringbuilder.append(addSpaces(" " + event.getDescription(), 45));
                }
                if (columns[3]) {
                    stringbuilder.append(addSpaces(" " + event.getGenre(), 15));
                }
                stringbuilder.append("\n");
            }
            stringbuilder.append(line).append("\n");
        }
        System.out.println(stringbuilder.toString());
    }
