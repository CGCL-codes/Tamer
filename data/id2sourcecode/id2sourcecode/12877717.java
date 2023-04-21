    public void transferTo(String pathLocal) throws IOException {
        if (isDir) {
            recursiveTransfer(pathLocal, true);
        } else {
            path = pathLocal + File.separator + name;
            FileOutputStream fileOut = new FileOutputStream(path);
            transferTo(fileOut.getChannel());
            fileOut.close();
        }
    }
