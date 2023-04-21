        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1) sw.write(c);
                sw.close();
                is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
