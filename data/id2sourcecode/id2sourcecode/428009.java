                    public void run() {
                        int c;
                        try {
                            while ((c = in.read()) != -1) System.err.write(c);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
