    private void comunicacionServer() {
        int in = 0;
        long tamFile = 0;
        URL pagina = this.getCodeBase();
        String protocolo = pagina.getProtocol();
        String servidor = pagina.getHost();
        int puerto = pagina.getPort();
        String servlet = "";
        if (this.urlService != null && !this.urlService.equals("")) servlet = this.urlService; else servlet = "/PruebaApplet/ServletEnviaReportes";
        URL direccion = null;
        URLConnection conexion = null;
        try {
            direccion = new URL(protocolo, servidor, puerto, servlet);
            conexion = direccion.openConnection();
            System.out.println(" Url del urlService " + direccion.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        conexion.setUseCaches(false);
        conexion.setRequestProperty("Accept-Language", "es");
        BufferedInputStream buffer = null;
        try {
            DataInputStream dataIn = new DataInputStream(conexion.getInputStream());
            buffer = new BufferedInputStream(conexion.getInputStream());
            int numArchivosRecibir = dataIn.readInt();
            System.out.println(" Numero de archivos a recibir " + numArchivosRecibir);
            String nameFiles[] = new String[numArchivosRecibir];
            File f = null;
            File files[] = new File[numArchivosRecibir];
            FileOutputStream ouputStream = null;
            for (int k = 0; k < numArchivosRecibir; k++) {
                f = new File(PATH + dataIn.readUTF());
                tamFile = dataIn.readLong();
                nameFiles[k] = f.getAbsolutePath();
                files[k] = f;
                System.out.println(" file no. " + k + " " + nameFiles[k] + " tama�o recibido " + tamFile);
                ouputStream = new FileOutputStream(f);
                for (int j = 0; j < tamFile; j++) {
                    ouputStream.write(buffer.read());
                }
                ouputStream.close();
                System.out.println(" Tama�o del archivo " + f.getName() + " : " + f.length());
            }
            dataIn.close();
            buffer.close();
            dataIn = null;
            buffer = null;
            ouputStream = null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
