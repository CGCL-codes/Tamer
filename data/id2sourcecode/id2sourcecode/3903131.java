    public String borrar(List<Proveedor> proveedor) throws Exception {
        Session session = getSession();
        Transaction trans = session.beginTransaction();
        String noBorrados = new String();
        List<Producto> pd;
        System.out.print("**************************Guardando en el Manager*********************************************  ");
        try {
            for (Proveedor p : proveedor) {
                pd = new ArrayList<Producto>(session.createQuery("select prod from Producto prod where prod.codproveedor =:p").setParameter("p", p).list());
                System.out.print("Resultado del query -----------> ");
                for (Producto q : pd) {
                    System.out.println(q.getNombre() + "  \n " + q.getDescripcion() + "  \n " + q.getCantidad() + "  \n " + q.getCodigo() + "  \n " + q.getCodproveedor().getNombre());
                }
                System.out.print("\nLista con cantidad de elementos igual a ***************  " + pd.size() + " ---------- \n");
                if (pd.size() == 0) {
                    System.out.println("Borrando a " + p.getNombre() + "\n");
                    session.createQuery("delete from Proveedor where codigo =:cod").setParameter("cod", p.getCodigo()).executeUpdate();
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
