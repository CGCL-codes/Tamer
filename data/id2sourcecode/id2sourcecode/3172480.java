    public Hashtable[] removeArrayElement(Hashtable[] cons, int i) {
        if (cons != null && cons.length > i) {
            Hashtable[] newhash = new Hashtable[cons.length - 1];
            for (int j = 0; j < i; j++) {
                newhash[j] = cons[j];
            }
            for (int j = i + 1; j < newhash.length; j++) {
                newhash[j] = cons[j + 1];
            }
            return newhash;
        }
        return null;
    }
