    public void remove(int cual) {
        if (cual > howmany) {
            System.out.println("No es posible extraer un elemento en esa posición.");
        } else {
            array[cual] = null;
            for (int i = cual; i < array.length - 1; i++) {
                array[i] = array[i + 1];
            }
            array[howmany] = null;
            howmany--;
        }
    }
