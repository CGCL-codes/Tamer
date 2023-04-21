    public NeuralNet restoreNeuralNet(String fileName) {
        try {
            FileInputStream stream = new FileInputStream(fileName);
            ObjectInputStream inp = new ObjectInputStream(stream);
            return (NeuralNet) inp.readObject();
        } catch (Exception excp) {
            try {
                String fn[] = fileName.split("/");
                System.out.println(fn[fn.length - 1]);
                URL url = Load.class.getResource(fileName);
                ObjectInputStream ins = new ObjectInputStream(url.openStream());
                return (NeuralNet) ins.readObject();
            } catch (Exception excp2) {
                excp2.printStackTrace();
                return null;
            }
        }
    }
