    public void run() {
        try {
            IHandler handler = Client.getClient().getReactor().getHandler();
            while (!Thread.interrupted() && alive) {
                String write = buffer.readLine();
                handler.write(TYPE_TEXT, write);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                System.out.println("bye");
            } else e.printStackTrace();
        }
    }
