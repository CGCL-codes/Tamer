    protected void receiveData(final LinkedList<Object[]> data) {
        try {
            File parentDirectory = new File(new URI(actionElement.getAttribute(directoryAttributeName)));
            if (!parentDirectory.exists()) {
                parentDirectory.mkdirs();
            }
            File file = new File(parentDirectory, getFileName());
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file, writeOnSameFile);
            FileChannel channel = out.getChannel();
            StringBuilder buffer = new StringBuilder();
            ByteBuffer byteBuffer = null;
            String sep = actionElement.getAttribute(sepBetweenFieldsAttributeName);
            String lineSep = System.getProperty("line.separator");
            if (displayColumnNames) {
                Object[] colNames = data.getFirst();
                for (int i = 0; i < colNames.length; i++) {
                    buffer.append((String) colNames[i] + sep);
                }
                buffer.append(lineSep);
                byteBuffer = ByteBuffer.wrap(buffer.toString().getBytes("UTF-8"));
                channel.write(byteBuffer);
                byteBuffer.clear();
            }
            data.removeFirst();
            if (data.size() > 0) {
                for (Object[] array : data) {
                    buffer = new StringBuilder();
                    for (Object obj : array) {
                        if (obj == null) {
                            obj = "";
                        }
                        if (obj instanceof Timestamp) {
                            obj = dateTimeFormat.format((Date) obj);
                        } else if (obj instanceof Date) {
                            obj = dateFormat.format((Date) obj);
                        }
                        buffer.append(obj + sep);
                    }
                    buffer.append(lineSep);
                    byteBuffer = ByteBuffer.wrap(buffer.toString().getBytes("UTF-8"));
                    channel.write(byteBuffer);
                    byteBuffer.clear();
                }
            }
            out.flush();
            out.close();
            channel.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
