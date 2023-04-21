    private int readFirstLine(BufferedReader reader) {
        try {
            String firstLine = reader.readLine();
            Matcher verticesNumber = GraphRegularExpressions.VERTICES_BEGINING.matcher(firstLine);
            if (verticesNumber.find()) return Integer.parseInt(verticesNumber.group(1)); else {
                LoggingManager.getInstance().writeSystem("Couldn't read fisrt line of the network file.\nFirst line: " + firstLine, "GraphLoader", "readFirstLine", null);
                return -1;
            }
        } catch (IOException ex) {
            LoggingManager.getInstance().writeSystem(ex.getMessage() + "\n" + ex.getStackTrace(), "GraphLoader", "readFirstLine", ex);
            return -1;
        }
    }
