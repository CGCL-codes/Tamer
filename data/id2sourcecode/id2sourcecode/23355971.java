    public static void clean() {
        try {
            RandomAccessFile in = new RandomAccessFile("Aluno.dat", "rw");
            RandomAccessFile out = new RandomAccessFile("Aluno.tmp", "rw");
            int high = in.readInt();
            out.writeInt(high);
            int idaux;
            String nome;
            String sobrenome;
            while (in.getFilePointer() < in.length()) {
                idaux = in.readInt();
                nome = in.readUTF();
                sobrenome = in.readUTF();
                if (idaux != 0) {
                    out.writeInt(idaux);
                    out.writeUTF(nome);
                    out.writeUTF(sobrenome);
                }
            }
            in.close();
            out.seek(0);
            int ch;
            FileOutputStream dat = new FileOutputStream("Aluno.dat");
            while ((ch = out.read()) != -1) dat.write(ch);
            dat.close();
            out.close();
            File newf = new File("Aluno.tmp");
            newf.delete();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
