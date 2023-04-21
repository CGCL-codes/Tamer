    public JComponent getComponent(ListaAcciones lista, PanelDatos Panel) {
        JPanel panel = new JPanel();
        JLabel imagen;
        try {
            URL url = new URL("http://www.infoclima.com/servicios/infograficos/img/ciudades/3.jpg");
            URLConnection urlCon = url.openConnection();
            InputStream is = urlCon.getInputStream();
            FileOutputStream fos = new FileOutputStream("clima.jpg");
            byte[] array = new byte[1000];
            int leido = is.read(array);
            while (leido > 0) {
                fos.write(array, 0, leido);
                leido = is.read(array);
            }
            is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageIcon icono = new ImageIcon("clima.jpg");
        imagen = new JLabel(icono);
        imagen.setSize(250, 250);
        imagen.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(imagen);
        panel.setSize(250, 250);
        return panel;
    }
