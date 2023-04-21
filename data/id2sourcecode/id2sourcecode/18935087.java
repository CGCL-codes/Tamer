    private byte[] hash() throws ErrorException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            throw new ErrorException("No such Algorithm");
        }
        byte[] arraybyte = null;
        long dim = sorgente.length();
        if (dim < BitCreekPeer.DIMBLOCCO) {
            arraybyte = new byte[(int) dim];
        } else {
            arraybyte = new byte[BitCreekPeer.DIMBLOCCO];
        }
        for (int i = 0; i < arraybyte.length; i++) {
            arraybyte[i] = 0;
        }
        FileInputStream input = null;
        try {
            input = new FileInputStream(sorgente);
        } catch (FileNotFoundException ex) {
            throw new ErrorException("File not Found");
        }
        byte[] arrayris = null;
        int dimhash = 0;
        ArrayList<byte[]> array = new ArrayList<byte[]>();
        try {
            while (input.read(arraybyte) != -1) {
                dim -= arraybyte.length;
                md.update(arraybyte);
                arrayris = md.digest();
                dimhash += arrayris.length;
                array.add(arrayris);
                if (dim != 0) {
                    if (dim < BitCreekPeer.DIMBLOCCO) {
                        arraybyte = new byte[(int) dim];
                    } else {
                        arraybyte = new byte[BitCreekPeer.DIMBLOCCO];
                    }
                    for (int i = 0; i < arraybyte.length; i++) {
                        arraybyte[i] = 0;
                    }
                }
            }
        } catch (IOException ex) {
            throw new ErrorException("IO Problem");
        }
        byte[] hash = new byte[dimhash];
        int k = 0;
        for (int i = 0; i < array.size(); i++) {
            arrayris = array.get(i);
            for (int j = 0; j < arrayris.length; j++) {
                hash[k] = arrayris[j];
                k++;
            }
        }
        return hash;
    }
