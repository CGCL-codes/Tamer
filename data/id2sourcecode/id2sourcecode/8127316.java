    public static void generarReporte(String url, String numero) {
        try {
            ManejadorBaseDatos basedatos = ManejadorBaseDatos.getInstancia();
            Map parameters = new HashMap();
            parameters.put("NumCotizacion", numero);
            basedatos.conectar();
            JasperPrint jasperPrint;
            jasperPrint = JasperFillManager.fillReport(vistaCotizacion.class.getResource(url).openStream(), parameters, basedatos.getConexion());
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setVisible(true);
        } catch (JRException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "mensaje de error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "mensaje de error", JOptionPane.ERROR_MESSAGE);
        }
    }
