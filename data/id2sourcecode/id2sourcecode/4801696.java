    @Override
    public void run() {
        try {
            int byteIn;
            while ((byteIn = input.read()) != -1) output.write(byteIn);
            if (output != null) output.close();
        } catch (IOException ioe) {
            System.out.println("Error during process pipe listening: " + ioe.getMessage());
        }
    }
