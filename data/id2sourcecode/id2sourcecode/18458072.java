    public void save() {
        try {
            lang = getCode();
            Scanner in = new Scanner(lang);
            try {
                fileOut = new PrintStream(savePath);
                while (in.hasNext()) {
                    fileOut.println(in.nextLine());
                }
                setStatus(savePath.toString());
            } catch (IOException e) {
            } catch (NullPointerException e) {
                saveAs();
            }
        } catch (Exception e) {
        }
    }
