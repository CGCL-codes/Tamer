    public void initializeArrays() {
        try {
            FileInputStream fstreamRead = new FileInputStream("InfoFiles/ReadAttributes.txt");
            DataInputStream inRead = new DataInputStream(fstreamRead);
            BufferedReader read = new BufferedReader(new InputStreamReader(inRead));
            FileInputStream fstreamWrite = new FileInputStream("InfoFiles/WriteAttributes.txt");
            DataInputStream inWrite = new DataInputStream(fstreamWrite);
            BufferedReader write = new BufferedReader(new InputStreamReader(inWrite));
            FileInputStream fstreamCall = new FileInputStream("InfoFiles/MethodsCalled.txt");
            DataInputStream inCall = new DataInputStream(fstreamCall);
            BufferedReader call = new BufferedReader(new InputStreamReader(inCall));
            for (int i = 0; i < readFileCount; i++) {
                readArray[i] = read.readLine();
            }
            for (int j = 0; j < writeFileCount; j++) {
                writeArray[j] = write.readLine();
            }
            for (int k = 0; k < methodCallFileCount; k++) {
                callArray[k] = call.readLine();
            }
            read.close();
            write.close();
            call.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
