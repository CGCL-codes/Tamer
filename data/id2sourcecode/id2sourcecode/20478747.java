            public void run() {
                try {
                    while (true) {
                        outputStream.write((reader.readLine() + "\n").getBytes());
                        outputStream.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
