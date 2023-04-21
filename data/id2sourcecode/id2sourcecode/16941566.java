        public void saveState(DataOutput output) throws IOException {
            output.writeInt(aux.length);
            output.write(aux);
            output.writeInt(data.length);
            output.write(data);
            output.writeInt(readPosition);
            output.writeInt(writePosition);
            output.writeInt(length);
        }
