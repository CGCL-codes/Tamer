        private void readObject(ObjectInputStream _ois) throws IOException, ClassNotFoundException {
            time_ = _ois.readLong();
            type_ = _ois.readInt();
            boolean b = _ois.readBoolean();
            if (b) {
                int size = _ois.readInt();
                data_ = new ByteArrayOutputStream(size);
                for (int i = 0; i < size; i++) data_.write(_ois.read());
            }
        }
