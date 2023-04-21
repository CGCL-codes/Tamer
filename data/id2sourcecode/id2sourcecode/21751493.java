    public final void run() {
        int retries = 0;
        if (tester == null || encrypt == null) {
            throw new NullPointerException("Dados de teste ou encriptação não inicializados.");
        }
        boolean end = false;
        while (!end) {
            lastUpdate = new Date(System.currentTimeMillis());
            if (retries == br.nic.connector.general.Constants.DEFAULT_MAXTESTTRIES) {
                retries = 0;
                currTestElement = tester.finishTestElement(currTestElement, null);
            }
            retries += 1;
            try {
                if (currTestElement == null) {
                    end = true;
                } else {
                    Object result = testThisElement(currTestElement);
                    currTestElement = tester.finishTestElement(currTestElement, result);
                    retries = 0;
                }
            } catch (Exception e) {
                try {
                    SimpleLog.getInstance().writeException(e, 3);
                    if (currTestElement == null) {
                        SimpleLog.getInstance().writeLog(3, "Finalizando Thread em exceção, pois " + "o elemento de testes atual é nulo.");
                        end = true;
                    } else {
                        SimpleLog.getInstance().writeLog(3, "Ocorrida em " + (currTestElement instanceof Object[] ? ((Object[]) currTestElement)[0].toString() : currTestElement.toString()));
                    }
                } catch (Exception ex) {
                    System.err.println("\nExceção no tratamento de exceção!\n");
                    ex.printStackTrace();
                }
            } catch (StackOverflowError e) {
                SimpleLog.getInstance().writeLog(3, "Erro de Stack Over Flow durante teste de " + tester.type + " no elemento " + testElementString());
            } catch (ThreadDeath err) {
                tester.writeTestData(currTestElement, null);
                end = true;
                System.out.println("Passou por aqui o " + testElementString());
            }
        }
        try {
            cleanup();
        } catch (Throwable t) {
        }
        tester.finishThread(this, true);
    }
