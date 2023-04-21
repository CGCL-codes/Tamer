    protected Object reload() throws IOException, ClassNotFoundException {
        if (xload || xdump) {
            ois = new ObjectInputStream(new FileInputStream(xFileName + "-" + getName() + ".ser"));
        } else {
            ois = new ObjectInputStream(new ByteArrayInputStream(bao.toByteArray()));
        }
        try {
            return ois.readObject();
        } finally {
            ois.close();
        }
    }
