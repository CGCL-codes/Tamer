    private static void joinLines(List<Line> list, String joinLineChar, StringDigester dig) {
        StringBuilder previous = null;
        int skipped = 0;
        Line myLine = null;
        for (ListIterator<Line> i = list.listIterator(); i.hasNext(); ) {
            myLine = i.next();
            String line = myLine.getText();
            if (dig != null) {
                line = dig.digest(line);
            }
            if (previous == null) {
                if (line.endsWith(joinLineChar)) {
                    line = line.substring(0, line.length() - 1).trim() + " ";
                    previous = new StringBuilder(line);
                    i.remove();
                    skipped++;
                } else {
                    if (line.length() == 0) {
                        i.remove();
                    } else {
                        if (dig != null) {
                            myLine.setText(line);
                        }
                    }
                }
            } else {
                if (joinLineChar != null && line.endsWith(joinLineChar)) {
                    line = line.substring(0, line.length() - 1).trim() + " ";
                    previous.append(line);
                    i.remove();
                    skipped++;
                } else {
                    line = previous.append(line).toString();
                    previous = null;
                    myLine.setText(line);
                    skipped = 0;
                }
            }
        }
        if (previous != null) {
            String line = myLine.getText();
            line = line.substring(0, line.length() - 1).trim();
            if (line.length() > 0) {
                list.add(myLine);
            }
        }
    }
