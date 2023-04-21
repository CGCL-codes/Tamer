    public String readFile(File f) {
        String input = "";
        try {
            FileInputStream fstream = new FileInputStream(f);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while (br.ready()) {
                input += br.readLine() + "\n";
            }
            if (input.length() > 1) input = input.substring(0, input.length() - 1);
            br.close();
            in.close();
            fstream.close();
        } catch (FileNotFoundException e) {
            writeError("reading from " + f.getAbsolutePath(), e);
        } catch (IOException e) {
            writeError("reading from " + f.getAbsolutePath(), e);
        }
        return input;
    }
