    private void writeTestcases(PrintWriter w) throws Exception {
        BufferedReader r = null;
        try {
            File fi = new File("commands.txt");
            r = new BufferedReader(new FileReader(fi));
            int count = 0;
            String s;
            while ((s = r.readLine()) != null) writeTestcase(w, s, count++);
            r.close();
        } catch (Exception e) {
            if (r != null) r.close();
            throw e;
        }
    }
