    public String borrar(List<Producto> producto) throws Exception {
        Session session = getSession();
        Transaction trans = session.beginTransaction();
        String noBorrados = new String();
        List<Compra> pd;
        System.out.print("**************************Guardando en el Manager*********************************************  ");
        try {
            for (Producto p : producto) {
                pd = new ArrayList<Compra>(session.createQuery("select comp from Compra comp where comp.codProducto =:c").setParameter("c", p).list());
                System.out.print("Resultado del query -----------> ");
                if (pd.size() == 0) {
                    System.out.println("Borrando a " + p.getNombre() + "\n");
                    session.createQuery("delete from Producto where codigo =:cod").setParameter("cod", p.getCodigo()).executeUpdate();
                } else {
                    noBorrados = noBorrados + p.getNombre() + "\n";
                    System.out.println("No se puede borrar\n");
                }
            }
            System.out.print("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            trans.commit();
            return noBorrados;
        } catch (Exception ex) {
            ex.printStackTrace();
            trans.rollback();
            throw ex;
        }
    }
