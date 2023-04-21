                    public void run() {
                        int c;
                        try {
                            while ((c = in.read()) != -1) System.out.write(c);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
