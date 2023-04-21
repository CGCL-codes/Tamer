    public void run() {
        try {
            Runtime r = Runtime.getRuntime();
            String[] a = { "sed", "-e", "s/Hello/Goodbye/" };
            synchronized (this) {
                p = r.exec(a);
                this.notifyAll();
            }
            OutputStream os = p.getOutputStream();
            PrintStream ps = new PrintStream(os);
            ps.println("Hello World");
            ps.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            System.exit(1);
        }
    }
