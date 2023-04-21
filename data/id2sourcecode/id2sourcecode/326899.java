    private void execute(String fileName) throws Exception {
        boolean dataStart = false;
        RandomAccessFile input = new RandomAccessFile(fileName, "r");
        AlgoData currentAlgoData = null;
        while (input.getFilePointer() < input.length()) {
            String line = input.readLine().trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.equals("*")) {
                dataStart = !dataStart;
                continue;
            }
            if (!dataStart) {
                currentAlgoData = new AlgoData(line);
                System.out.println("reading data for =" + line);
                output.add(currentAlgoData);
            } else {
                currentAlgoData.data.addElement(line);
            }
        }
        System.out.println("finished reading. Time to write");
        input.close();
        String outString = createStringForFile();
        RandomAccessFile outputFile = new RandomAccessFile(outputFileName, "rw");
        outputFile.writeBytes(outString);
        outputFile.close();
    }
