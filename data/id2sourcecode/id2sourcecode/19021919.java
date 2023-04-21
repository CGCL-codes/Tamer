        public void run() {
            flag_download_finish = false;
            if (directory == null) {
                System.out.println("ERROR: no se ha indicado el directorio a conectarse.");
                System.out.println("ERROR: directory not specified.");
                System.out.println("Contactar con los desarrolladores / Contact with the developers.");
                System.exit(0);
            }
            flag_download_finish = false;
            if (file_name != null) {
                java.io.BufferedReader br = null;
                try {
                    java.net.URL url = new java.net.URL("http", "rolgps.sourceforge.net", 80, "/" + directory + "/" + file_name);
                    java.net.URLConnection uconect = url.openConnection();
                    conexion.setText("Conectando...");
                    conexion.repaint();
                    InputStream is = uconect.getInputStream();
                    byte b[] = new byte[1];
                    java.io.BufferedInputStream bis = new java.io.BufferedInputStream(is);
                    int n = 0;
                    java.io.File f_viejo = new java.io.File(file_name);
                    f_viejo.delete();
                    java.io.FileOutputStream fo = new java.io.FileOutputStream(new File(file_name));
                    while ((n = bis.read(b, 0, 1)) >= 0) {
                        fo.write(b);
                    }
                    fo.close();
                    is.close();
                    bis.close();
                    conexion.setText("Descarga completada...");
                } catch (java.io.FileNotFoundException fnfex) {
                    conexion.setText("No se ha encontrado en la base de datos del servidor el fichero " + file_name);
                } catch (Exception e) {
                    conexion.setText("Ha habido un problema con la descarga.");
                    System.out.println(e);
                }
            }
            flag_download_finish = true;
        }
