        public void run() {
            try {
                final CachedRandomAccessFile readRandAccessFile = filemanager.getFileToWrite();
                String refToObject = object.toString();
                final ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
                final ObjectOutputStream obj_out = new ObjectOutputStream(byteOutStream);
                synchronized (object) {
                    obj_out.writeObject(object);
                }
                byte[] byteArr = byteOutStream.toByteArray();
                obj_out.close();
                byteOutStream.close();
                final Integer length = byteArr.length;
                final String path = readRandAccessFile.getFile().getPath();
                final Long filePointer = readRandAccessFile.length();
                readRandAccessFile.seek(filePointer);
                readRandAccessFile.write(byteArr);
                filemanager.returnFile(readRandAccessFile);
                byteArr = null;
                map.put(refToObject, new FileObjectPointer(path, filePointer, length));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
