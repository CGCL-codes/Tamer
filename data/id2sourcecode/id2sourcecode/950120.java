    public static void main(String[] args) throws Exception {
        int nBytes;
        ServerSocket serv = null;
        Socket s = null;
        String so = new String("");
        so = System.getProperty("os.name");
        System.out.println("Sistema : " + so);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String nomeLog = "log" + sf.format(new Date()) + ".txt";
        FileWriter fw = new FileWriter(new File(nomeLog));
        while (true) {
            serv = new ServerSocket(7000);
            System.out.println("\nAguardando transmissao... ");
            s = serv.accept();
            BufferedInputStream in = new BufferedInputStream(new GZIPInputStream(s.getInputStream()));
            byte aux[] = new byte[30];
            byte cop[] = new byte[2];
            nBytes = in.read(aux);
            String nomearq = new String(aux);
            nBytes = in.read(aux);
            String destino = new String(aux);
            nBytes = in.read(cop);
            String copias = new String(cop);
            System.out.println("Recebendo Arquivo: " + nomearq.toString());
            System.out.println("Fila de Impressao: " + destino.toString());
            System.out.println("Nro de Copias....: " + copias.toString());
            fw.write("Relatorio: " + nomearq + "  copias: " + copias);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("c:" + nomearq));
            byte b[] = new byte[1024];
            while ((nBytes = in.read(b)) != -1) {
                out.write(b, 0, nBytes);
            }
            out.flush();
            out.close();
            s.close();
            serv.close();
            int i = 0;
            int vezes = Integer.parseInt(copias);
            if (so.matches("Windows 98")) {
                for (i = 0; i < vezes; i++) {
                    Runtime.getRuntime().exec("c:/command.com /C type c:" + nomearq + " > " + destino.toString());
                }
                Runtime.getRuntime().exec("c:/command.com /C del c:" + nomearq);
            } else {
                for (i = 0; i < vezes; i++) {
                    Runtime.getRuntime().exec("cmd /C type c:" + nomearq + " > " + destino.toString());
                }
                Runtime.getRuntime().exec("cmd /C del c:" + nomearq);
            }
        }
    }
