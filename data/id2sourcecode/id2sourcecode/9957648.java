        public void run() {
            try {
                int i;
                while ((i = is.read()) != -1) os.write(i);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
