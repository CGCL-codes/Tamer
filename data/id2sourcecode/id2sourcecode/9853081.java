                    @Override
                    public void run() {
                        try {
                            int read;
                            while ((read = is.read()) != -1) {
                                sw.write(read);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
