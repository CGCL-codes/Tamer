    public void executa() {
        String so = System.getProperty("os.name");
        System.out.println("Sistema : " + so);
        String prefixo = null;
        if (so.matches("Windows 98")) prefixo = "c:/command.com /C "; else prefixo = "cmd /C ";
        Hashtable tab = new Hashtable(8);
        tab.put("lpt2", "");
        tab.put("lpt3", "");
        tab.put("lpt4", "");
        tab.put("lpt5", "");
        tab.put("lpt6", "");
        tab.put("lpt7", "");
        tab.put("lpt8", "");
        tab.put("lpt9", "");
        String[] portas = { "lpt2", "lpt3", "lpt4", "lpt5", "lpt6", "lpt7", "lpt8", "lpt9" };
        String comando = new String("");
        for (int p = 0; p < 8; p++) {
            comando = prefixo + "net use " + portas[p] + ": /delete";
            System.out.println(comando);
            try {
                Runtime.getRuntime().exec(comando);
                Thread.sleep(2000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        ServerSocket serv = null;
        try {
            serv = new ServerSocket(7000);
        } catch (IOException e) {
            e.getMessage();
        }
        Socket s = null;
        while (true) {
            try {
                System.out.println("\nAguardando transmissao... ");
                s = serv.accept();
            } catch (SocketException se) {
                System.err.println(se.getCause());
            } catch (IOException ie) {
                System.err.println(ie.getMessage());
                System.err.println(ie.getCause());
            }
            BufferedInputStream in = null;
            try {
                in = new BufferedInputStream(new GZIPInputStream(s.getInputStream()));
            } catch (SocketException se) {
                System.err.println(se.getMessage());
                System.err.println(se.getCause());
            } catch (IOException ie) {
                System.err.println(ie.getMessage());
                System.err.println(ie.getCause());
                ie.printStackTrace();
            }
            byte aux[] = new byte[30];
            byte cop[] = new byte[2];
            String nomearq = null;
            String destino = null;
            String copias = null;
            try {
                in.read(aux);
                nomearq = new String(aux);
                in.read(aux);
                destino = new String(aux);
                in.read(cop);
                copias = new String(cop);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Recebendo Arquivo: " + nomearq);
            System.out.println("Fila de Impressao: " + destino);
            System.out.println("Nro de Copias....: " + copias);
            BufferedOutputStream out = null;
            try {
                out = new BufferedOutputStream(new FileOutputStream("c:" + nomearq));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int nBytes;
            byte buff[] = new byte[1024];
            try {
                while ((nBytes = in.read(buff)) != -1) out.write(buff, 0, nBytes);
                out.flush();
                out.close();
                in.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Arquivo recebido, iniciando impressao...");
            int vezes = Integer.parseInt(copias);
            StringBuffer comandoNetu = new StringBuffer("");
            StringBuffer comandoImpr = new StringBuffer("");
            StringBuffer comandoDele = new StringBuffer("");
            StringBuffer comandoNetd = new StringBuffer("");
            String porta = "";
            boolean remapear = false;
            for (int p = 0; p < tab.size(); p++) {
                if ("".equals(tab.get(portas[p]))) {
                    tab.put(portas[p], destino);
                    porta = portas[p];
                    break;
                } else {
                    if (destino.equals(tab.get(portas[p]))) {
                        porta = portas[p];
                        break;
                    }
                }
            }
            if ("".equals(porta)) {
                remapear = true;
                porta = "lpt9";
                tab.put(porta, destino);
            }
            comandoNetd.append(prefixo + "net use " + porta + ": /delete ");
            comandoNetu.append(prefixo + "net use " + porta + ": " + destino);
            comandoImpr.append(prefixo + "type c:" + nomearq.trim() + " > " + porta);
            comandoDele.append(prefixo + "del c:" + nomearq);
            if (!Util.TestaImpressora(destino)) System.out.println("Impressora " + destino.trim() + " inoperante."); else {
                if (remapear) {
                    Util.executeCommand(comandoNetd.toString(), 3000);
                }
                Util.executeCommand(comandoNetu.toString(), 21000);
                for (int i = 0; i < vezes; i++) {
                    System.out.println("Imprimindo " + nomearq.trim() + " em " + destino.trim() + " " + (i + 1) + " de " + vezes);
                    Util.executeCommand(comandoImpr.toString(), 3000);
                }
            }
            Util.executeCommand(comandoDele.toString(), 3000);
        }
    }
