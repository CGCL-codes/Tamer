        public void loadState(DataInput input) throws IOException {
            int len = input.readInt();
            aux = new byte[len];
            input.readFully(aux, 0, len);
            len = input.readInt();
            data = new byte[len];
            input.readFully(data, 0, len);
            readPosition = input.readInt();
            writePosition = input.readInt();
            length = input.readInt();
        }
