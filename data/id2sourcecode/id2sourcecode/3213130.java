    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(FileBundle.getFile("config/asciiLogo", ".txt", EngineJogo.class.getClassLoader()), "utf-8");
        while (scanner.hasNextLine()) System.out.println(FileBundle.addKeyValues(scanner.nextLine(), messageStrings));
        System.out.println();
        System.out.println(messageStrings.getProperty("loading"));
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                acabou = true;
            }
        });
        try {
            adicionaSubEngines();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("#################################################################");
        System.out.println("#################### STARTING GAME ##############################");
        System.out.println("#################################################################");
        long now = System.nanoTime();
        while (!acabou) {
            SubEngine engAtual = subEngines.first();
            subEngines.remove(engAtual);
            long delay;
            do {
                delay = engAtual.getInstanteProximaExecucao() - System.nanoTime();
                if (delay > 0) try {
                    Thread.sleep(delay / 1000000000);
                } catch (Exception e) {
                }
            } while (delay > 0);
            now = System.nanoTime();
            long ultimaExecucao = engAtual.getInstanteUltimaExecucao();
            {
                String nome = engAtual.getClass().getName();
                int ind = nome.lastIndexOf(".");
                nome = nome.substring(ind + 1);
            }
            try {
                engAtual.run();
                subEngines.add(engAtual);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            float deltaTmpExecucao = (engAtual.getInstanteUltimaExecucao() - ultimaExecucao);
            float fps = 0;
            float percCPU = 0;
            if (deltaTmpExecucao > 0) {
                fps = 1000000000f / deltaTmpExecucao;
                percCPU = 100 * (System.nanoTime() - now) / (deltaTmpExecucao);
            }
            java.text.DecimalFormat nf = new java.text.DecimalFormat();
            nf.setMaximumFractionDigits(3);
            nf.setMaximumIntegerDigits(2);
            nf.setMinimumIntegerDigits(2);
            if (false) System.out.println(nf.format((System.nanoTime() - now) / 1000000f) + "ms " + nf.format(fps) + " fps) -> " + nf.format(percCPU) + "% CPU");
        }
        try {
            termina();
        } catch (Throwable t) {
        }
    }
