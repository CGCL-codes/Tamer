    public List<Regla> planificar(IBaseConocimientos bc) {
        List<Regla> reglas = bc.getReglas();
        Collections.sort(reglas);
        Collection<Sensor> estado = bc.getEstadoActual();
        DefaultMutableTreeNode nodo = null;
        Iterator i = reglas.iterator();
        while (i.hasNext() && nodo == null) {
            Regla regla = (Regla) i.next();
            DefaultMutableTreeNode root = armarArbol(regla.getPredicciones(), reglas);
            nodo = buscarNodo(root, estado);
        }
        if (nodo == null) {
            List<Regla> plan = new ArrayList<Regla>();
            int count = reglas.size();
            Random r = new Random();
            int number = r.nextInt(count + 1);
            Regla regla = reglas.get(number);
            plan.add(regla);
            return plan;
        } else {
            return armarCamino(nodo, reglas);
        }
    }
