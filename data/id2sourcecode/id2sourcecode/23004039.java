    public ELF loadFirmware(URL url, int[] memory) throws IOException {
        DataInputStream inputStream = new DataInputStream(url.openStream());
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] firmwareData = new byte[2048];
        int read;
        while ((read = inputStream.read(firmwareData)) != -1) {
            byteStream.write(firmwareData, 0, read);
        }
        inputStream.close();
        ELF elf = new ELF(byteStream.toByteArray());
        elf.readAll();
        return loadFirmware(elf, memory);
    }
