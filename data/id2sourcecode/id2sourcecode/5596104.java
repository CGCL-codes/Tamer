    private boolean sendFile(String name) {
        StorageHandler sh = new StorageHandler(name, this.getApplicationContext());
        Date before = new Date();
        if (!sh.openRead()) return false;
        String str = sh.read();
        while (str != null) {
            thread.write(str);
            if (MODO.equalsIgnoreCase("Espera")) {
                try {
                    Thread.sleep(TEMPO_ESPERA);
                } catch (Exception e) {
                    Log.i(TAG, "Caiu na exce��o.", e);
                }
            }
            str = sh.read();
        }
        sh.close();
        Date after = new Date();
        long deltaTime = after.getTime() - before.getTime();
        Log.i(TAG, "Enviou " + thread.getBytesSent() + " bytes em " + deltaTime + " milissegundos.");
        sentView.setText("Enviou " + thread.getBytesSent() + " bytes em " + deltaTime + " milissegundos.");
        thread.close();
        this.close();
        return true;
    }
