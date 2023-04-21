    @Override
    public void loadData(URL url, OutputStream output) {
        try {
            System.out.print("Loading " + url + ": ");
            InputStream is = url.openStream();
            try {
                byte[] buffer = new byte[4096];
                while (true) {
                    int count = is.read(buffer);
                    if (count == -1) {
                        break;
                    }
                    output.write(buffer, 0, count);
                    System.out.print("#");
                }
            } finally {
                System.out.println(" - finished");
                is.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
