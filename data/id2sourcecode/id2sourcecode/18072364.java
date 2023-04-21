            public Object run() {
                String marshallerName = Marshaller.class.getName();
                InputStream stream = Marshaller.class.getResourceAsStream(marshallerName.substring(marshallerName.lastIndexOf('.') + 1) + ".class");
                if (stream == null) throw new Error("Could not load implementation class " + marshallerName);
                BufferedInputStream bis = new BufferedInputStream(stream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(baos);
                try {
                    byte[] buffer = new byte[256];
                    int read = -1;
                    while ((read = bis.read(buffer)) >= 0) bos.write(buffer, 0, read);
                    bis.close();
                    bos.close();
                } catch (IOException x) {
                    throw new Error(x.toString());
                }
                byte[] classBytes = baos.toByteArray();
                MarshallerClassLoader loader = new MarshallerClassLoader(classBytes);
                try {
                    Class cls = loader.loadClass(marshallerName);
                    return cls.getMethod("unmarshal", new Class[] { MarshalledObject.class });
                } catch (ClassNotFoundException x) {
                    throw new Error(x.toString());
                } catch (NoSuchMethodException x) {
                    throw new Error(x.toString());
                }
            }
