    public static void imprime(String arqp) {
        String drive = arqp.substring(0, 2);
        if (drive.indexOf(":") == -1) drive = "";
        Properties p = Util.lerPropriedades(arqp);
        String serv = new String(p.getProperty("servidor"));
        String fila = new String(p.getProperty("fila"));
        String arqRel = new String(drive + p.getProperty("arquivo"));
        String copias = new String(p.getProperty("copias"));
        String usuario = new String(p.getProperty("usuario"));
        if (usuario == null) usuario = new String("indefinido");
        Hashtable hash = new Hashtable(4);
        hash.put(Context.INITIAL_CONTEXT_FACTORY, Environment.QMS_INITIAL_CONTEXT_FACTORY);
        hash.put(Context.PROVIDER_URL, serv);
        hash.put(QMSEnvironment.QUEUE_NAME, fila);
        try {
            QMSQueue testQueue = (QMSQueue) new InitialDirContext(hash).lookup("");
            QMSJob job = testQueue.createJob();
            if (usuario == null) job.setDescription(""); else job.setDescription(usuario);
            byte cra[] = new byte[152];
            for (int i = 0; i < 152; i++) {
                cra[i] = 0x00;
            }
            cra[2] = 0x00;
            cra[3] = (byte) Integer.parseInt(copias);
            cra[4] = 0x00;
            cra[5] = 0x08;
            job.setClientRecordArea(cra);
            QMSOutputStream os = job.submit();
            System.out.println("Imprimindo " + arqRel + " em " + serv + "/" + fila + " cop:" + copias);
            FileInputStream fis = new FileInputStream(new File(arqRel));
            byte inbuf[] = new byte[1024];
            int nBytes;
            while ((nBytes = fis.read(inbuf)) != -1) os.write(inbuf, 0, nBytes);
            os.close();
            fis.close();
            File f = new File(arqp);
            f.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
