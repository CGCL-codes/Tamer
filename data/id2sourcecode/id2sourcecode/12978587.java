        public boolean onData(INonBlockingConnection con) throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException {
            String read = con.readStringByDelimiter(PROTOCOL_DELIMITER);
            System.out.println("[" + con.getId() + "] Server Data Received read: " + read);
            Random rand = new Random();
            long sleepTime = (long) (1000 * rand.nextDouble());
            try {
                System.out.println("[" + con.getId() + "] Server Thread. Sleeping for " + sleepTime + " ms");
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("[" + con.getId() + "] Server Thread Filling Large StringBuffer");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 1000000; i++) {
                sb.append(i);
            }
            sb.setLength(0);
            con.write("Echo " + read + PROTOCOL_DELIMITER);
            System.out.println("[" + con.getId() + "] Server Sent Echo read " + read);
            return true;
        }
