        public boolean load(InputStream stream, Model model) {
            istream = new DataInputStream(stream);
            try {
                offset = 0;
                int ident = nextInt();
                if (ident != 844121161) {
                    write("Invalid file identifier: " + ident);
                    close();
                    return false;
                }
                int version = nextInt();
                if (version != 8) {
                    write("Invalid file version: " + version);
                    close();
                    return false;
                }
                int skinWidth = nextInt();
                int skinHeight = nextInt();
                nextInt();
                int numSkins = nextInt();
                int numVertices = nextInt();
                int numTexCoords = nextInt();
                int numTriangles = nextInt();
                int numGLCommands = nextInt();
                int numFrames = nextInt();
                int offsetSkins = nextInt();
                int offsetTexCoords = nextInt();
                int offsetTriangles = nextInt();
                int offsetFrames = nextInt();
                int offsetGLCommands = nextInt();
                int offsetEnd = nextInt();
                write("Skins:                  " + numSkins);
                write("Vertices:               " + numVertices);
                write("Texture Coords:         " + numTexCoords);
                write("Triangles:              " + numTriangles);
                write("GL Commands:            " + numGLCommands);
                write("Frames:                 " + numFrames);
                write("Skin offset:            " + offsetSkins);
                write("Texture Coords offset:  " + offsetTexCoords);
                write("Triangles offset:       " + offsetTriangles);
                write("Frames offset:          " + offsetFrames);
                write("GL Commands offset:     " + offsetGLCommands);
                write("End offset:             " + offsetEnd);
                if (numSkins > 0) {
                    skipTo(offsetSkins);
                    write("Skins (" + numSkins + ")");
                    for (int i = 0; i < numSkins; i++) {
                        String filename = nextString(64);
                        write("    " + (i) + ": " + filename);
                    }
                }
                ArrayList<UVCoord> uvs = new ArrayList<UVCoord>();
                if (numTexCoords > 0) {
                    skipTo(offsetTexCoords);
                    write("Texture coordinates (" + numTexCoords + ")");
                    for (int i = 0; i < numTexCoords; i++) {
                        float u = (float) nextShort() / skinWidth;
                        float v = (float) nextShort() / skinHeight;
                        write("    " + (i) + ": " + u + ", " + v);
                        uvs.add(new UVCoord(u, v));
                    }
                }
                if (numTriangles > 0) {
                    skipTo(offsetTriangles);
                    write("Triangles (" + numTriangles + ")");
                    for (int i = 0; i < numTriangles; i++) {
                        short v1 = nextShort();
                        short v2 = nextShort();
                        short v3 = nextShort();
                        short uv1 = nextShort();
                        short uv2 = nextShort();
                        short uv3 = nextShort();
                        Triangle t = new Triangle(model, v1, v2, v3, uvs.get(uv1), uvs.get(uv2), uvs.get(uv3));
                        model.addTriangle(t);
                        write("    " + (i) + ": vertices (" + v1 + "," + v2 + "," + v3 + "), tex coords (" + uv1 + "," + uv2 + "," + uv3 + ")");
                    }
                }
                if (numFrames > 0) {
                    skipTo(offsetFrames);
                    write("Frames (" + numFrames + ")");
                    for (int i = 0; i < numFrames; i++) {
                        float sclx = nextFloat();
                        float scly = nextFloat();
                        float sclz = nextFloat();
                        float transx = nextFloat();
                        float transy = nextFloat();
                        float transz = nextFloat();
                        String name = nextString(16);
                        write("    " + (i) + ": " + name);
                        model.addFrame();
                        write("      vertices: ");
                        for (int j = 0; j < numVertices; j++) {
                            short x = nextUnsignedByte();
                            short y = nextUnsignedByte();
                            short z = nextUnsignedByte();
                            short n = nextUnsignedByte();
                            n -= 2;
                            write("        -> " + (j) + ": " + x + ", " + y + ", " + z + ", " + n);
                            Vertex v = new Vertex(x, y, z, new Vector(normals[n][0], normals[n][1], normals[n][2]));
                            model.addVertex(v);
                        }
                    }
                }
                write("File reading reached " + offset + "/" + offsetEnd);
                model.trim();
                close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }
