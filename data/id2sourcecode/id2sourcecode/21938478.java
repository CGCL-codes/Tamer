    private String writeInternalStatic(BufferedReader reader, PrintWriter writer, String[] variables, int methodNumber) throws IOException {
        writer.println("    // internal method created by adjust");
        writer.println("    private static void internalStatic" + methodNumber + "()");
        writer.println("    {");
        for (int counter = 0; counter < variables.length; counter++) {
            writer.println(variables[counter]);
        }
        writer.println(START_OF_METHOD);
        String line = null;
        int methodCounter = 0;
        while ((line = reader.readLine()) != null) {
            if (line.equals(START_OF_METHOD)) {
                methodCounter++;
                if (methodCounter == MAX_METHODS) {
                    break;
                }
            }
            if (line.equals(END_OF_METHOD)) {
                break;
            }
            writer.println(line);
        }
        writer.println("    }");
        writer.println();
        return line;
    }
