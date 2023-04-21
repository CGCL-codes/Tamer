    public void arrancaHiloDescargas() {
        while (true) {
            ASalvar a = null;
            try {
                synchronized (lista) {
                    if (lista.size() == 0) lista.wait();
                    a = lista.get(0);
                    lista.remove(0);
                }
                avisaComienzaDescarga(a.url.toString());
                a.fichero = getPathYNombreNoExistente(new File(a.fichero)).getAbsolutePath();
                System.out.println(a.url.toString() + " -> " + a.fichero);
                URLConnection urlcon = a.url.openConnection();
                urlcon.connect();
                InputStream is = urlcon.getInputStream();
                byte[] buffer = new byte[1000];
                int leido = is.read(buffer);
                FileOutputStream fw = new FileOutputStream(a.fichero);
                while (leido > 0) {
                    fw.write(buffer, 0, leido);
                    leido = is.read(buffer);
                }
                fw.close();
                is.close();
                avisaTerminadaDescarga(a.url.toString(), true, null);
            } catch (Exception e) {
                if (null != a.url) {
                    avisaTerminadaDescarga(a.url.toString(), false, e);
                }
                e.printStackTrace();
            }
        }
    }
