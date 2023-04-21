    public static void writeArrayListToScreen(ArrayList<String[]> readArrrayList) {
        for (Iterator<String[]> it = readArrrayList.iterator(); it.hasNext(); ) {
            String[] tempStrings = it.next();
            String outputString = new String();
            for (String eachString : tempStrings) {
                outputString = outputString + eachString + " ";
            }
            System.out.println(outputString);
            outputString = null;
        }
    }
