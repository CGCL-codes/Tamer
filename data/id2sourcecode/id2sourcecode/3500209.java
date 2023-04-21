    private static String[] processHeader(int numberOfColumns, String[] parsedline, String filename) {
        if (parsedline[0].equalsIgnoreCase("<Header")) {
            String headername = parsedline[1].split("[=>\\s]")[1];
            if (!parsedline[1].contains("name=") || headername.length() == 0) {
                StringBuilder msg = new StringBuilder("Error in ");
                msg.append(filename);
                msg.append(": Improperly formatted <Header name=> line.");
                throw new IllegalArgumentException(msg.toString());
            }
            int finalBracketPosition = 0;
            while (!parsedline[finalBracketPosition].contains(">")) finalBracketPosition++;
            if (numberOfColumns == 0) numberOfColumns = parsedline.length - finalBracketPosition - 1; else if (numberOfColumns != parsedline.length - finalBracketPosition - 1) {
                StringBuilder msg = new StringBuilder("Error in ");
                msg.append(filename);
                msg.append(": The number of ");
                msg.append(parsedline[0]).append(" ").append(parsedline[1]);
                msg.append(" columns does not match the number of columns in previous header rows");
                throw new IllegalArgumentException(msg.toString());
            }
            String[] contents = new String[numberOfColumns + 1];
            contents[0] = headername;
            for (int i = 0; i < numberOfColumns; i++) {
                contents[i + 1] = parsedline[i + finalBracketPosition + 1];
            }
            return contents;
        } else {
            if (numberOfColumns == 0) numberOfColumns = parsedline.length - 1; else if (numberOfColumns != parsedline.length - 1) {
                StringBuilder msg = new StringBuilder("Error in ");
                msg.append(filename);
                msg.append(": The number of ");
                msg.append(parsedline[0]);
                msg.append(" columns does not match the number of columns in previous header rows");
                throw new IllegalArgumentException(msg.toString());
            }
            String[] contents = new String[numberOfColumns];
            for (int i = 0; i < numberOfColumns; i++) {
                contents[i] = parsedline[i + 1];
            }
            return contents;
        }
    }
