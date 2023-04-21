    public void saveBinder(String filename) {
        if (filename.endsWith(".crutch") | filename.endsWith(".CRUTCH")) {
        } else {
            filename = filename + ".crutch";
        }
        File file = new File(filename);
        try {
            FileOutputStream fileOutput = new FileOutputStream(file);
            DataOutputStream dataOut = new DataOutputStream(fileOutput);
            dataOut.writeUTF(title);
            for (int k = 0; k < rings.size(); k++) {
                dataOut.writeUTF(readNoteTitle(k));
                String tempString = new String(readNoteBody(k));
                dataOut.writeUTF(tempString);
            }
            fileOutput.close();
        } catch (IOException e) {
            System.out.println("There was an error: " + e);
        }
    }
