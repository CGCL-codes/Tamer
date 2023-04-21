        public void processRequest(java.io.ObjectOutputStream oos, java.io.ObjectInputStream ois, java.net.Socket socket, org.unicore.upl.ServerRequest sr) throws Exception {
            ObjectOutputStream to_njs_oos = null;
            ObjectInputStream from_njs_ois = null;
            try {
                to_njs_oos = new ObjectOutputStream(to_njs.getOutputStream());
                to_njs_oos.writeObject(ois.readObject());
                to_njs_oos.writeObject(ois.readObject());
                to_njs_oos.writeObject(ois.readObject());
                to_njs_oos.flush();
                from_njs_ois = new ObjectInputStream(to_njs.getInputStream());
                Integer hdr = (Integer) from_njs_ois.readObject();
                oos.writeObject(hdr);
                if (hdr.intValue() == 0) {
                    oos.writeObject(from_njs_ois.readObject());
                    oos.writeObject(from_njs_ois.readObject());
                    oos.writeObject(from_njs_ois.readObject());
                } else {
                    oos.writeObject(from_njs_ois.readObject());
                }
            } finally {
                try {
                    to_njs_oos.close();
                    from_njs_ois.close();
                } catch (Exception ex) {
                }
            }
        }
