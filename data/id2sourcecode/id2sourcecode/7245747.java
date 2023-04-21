    private void saveArquivo(String nomeArq, String modelo) {
        try {
            URL urlServlet = new URL(getCodeBase(), "./salvador?arq=" + user + "/" + nomeArq);
            System.out.println("user " + user);
            URLConnection con = urlServlet.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-java-serialized-object");
            OutputStream outstream = con.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outstream);
            oos.writeObject(modelo);
            oos.flush();
            oos.close();
            InputStream instr = con.getInputStream();
            ObjectInputStream inputFromServlet = new ObjectInputStream(instr);
            String result = (String) inputFromServlet.readObject();
            inputFromServlet.close();
            instr.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
