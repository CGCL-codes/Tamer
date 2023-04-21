        private void writeQuad() throws IOException {
            zos.putNextEntry(new ZipEntry("quad.bin"));
            Gbl.startMeasurement();
            quad = new OTFServerQuad(net);
            System.out.print("build Quad on Server: ");
            Gbl.printElapsedTime();
            onAdditionalQuadData();
            Gbl.startMeasurement();
            quad.fillQuadTree(new OTFDefaultNetWriterFactoryImpl());
            System.out.print("fill writer Quad on Server: ");
            Gbl.printElapsedTime();
            Gbl.startMeasurement();
            new ObjectOutputStream(zos).writeObject(quad);
            zos.closeEntry();
        }
