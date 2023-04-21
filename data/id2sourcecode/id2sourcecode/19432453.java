        private void unpackSegment(InputStream in, JarOutputStream out) throws IOException {
            _props.setProperty(java.util.jar.Pack200.Unpacker.PROGRESS, "0");
            new PackageReader(pkg, in).read();
            if (_props.getBoolean("unpack.strip.debug")) pkg.stripAttributeKind("Debug");
            if (_props.getBoolean("unpack.strip.compile")) pkg.stripAttributeKind("Compile");
            _props.setProperty(java.util.jar.Pack200.Unpacker.PROGRESS, "50");
            pkg.ensureAllClassFiles();
            HashSet classesToWrite = new HashSet(pkg.getClasses());
            for (Iterator i = pkg.getFiles().iterator(); i.hasNext(); ) {
                Package.File file = (Package.File) i.next();
                String name = file.nameString;
                JarEntry je = new JarEntry(Utils.getJarEntryName(name));
                boolean deflate;
                deflate = (keepDeflateHint) ? (((file.options & Constants.FO_DEFLATE_HINT) != 0) || ((pkg.default_options & Constants.AO_DEFLATE_HINT) != 0)) : deflateHint;
                boolean needCRC = !deflate;
                if (needCRC) crc.reset();
                bufOut.reset();
                if (file.isClassStub()) {
                    Package.Class cls = file.getStubClass();
                    assert (cls != null);
                    new ClassWriter(cls, needCRC ? crcOut : bufOut).write();
                    classesToWrite.remove(cls);
                } else {
                    file.writeTo(needCRC ? crcOut : bufOut);
                }
                je.setMethod(deflate ? JarEntry.DEFLATED : JarEntry.STORED);
                if (needCRC) {
                    if (verbose > 0) Utils.log.info("stored size=" + bufOut.size() + " and crc=" + crc.getValue());
                    je.setMethod(JarEntry.STORED);
                    je.setSize(bufOut.size());
                    je.setCrc(crc.getValue());
                }
                if (keepModtime) {
                    je.setTime(file.modtime);
                    je.setTime((long) file.modtime * 1000);
                } else {
                    je.setTime((long) modtime * 1000);
                }
                out.putNextEntry(je);
                bufOut.writeTo(out);
                out.closeEntry();
                if (verbose > 0) Utils.log.info("Writing " + Utils.zeString((ZipEntry) je));
            }
            assert (classesToWrite.isEmpty());
            _props.setProperty(java.util.jar.Pack200.Unpacker.PROGRESS, "100");
            pkg.reset();
        }
