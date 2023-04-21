    public static void main(String[] args) {
        HSQLDataSource ds = new HSQLDataSource();
        Connection conn;
        try {
            conn = ds.getConnection();
            int indice = 0;
            int fallo = 0;
            final PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT COORD_REF_SYS_CODE, COORD_REF_SYS_NAME" + " FROM epsg_coordinatereferencesystem ");
            ResultSet result;
            result = stmt.executeQuery();
            String filename = "salida.txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            while (result.next()) {
                ICrs crs = null;
                int cod = result.getInt("COORD_REF_SYS_CODE");
                try {
                    crs = new CrsFactory().getCRS("EPSG:" + cod);
                    if (crs instanceof CrsGT) {
                        if (crs.getProj4String() == null) {
                            bw.write("C�digo: " + cod + "\tNombre: " + result.getString("COORD_REF_SYS_NAME") + "\n");
                            bw.write("Cadena WKT: " + crs.getWKT() + "\n");
                            bw.write("Cadena Proj4: No cadena proj4 \n");
                            bw.write("Fallo: Fallo generando en cadena proj4 \n");
                            bw.write("\n\n");
                        }
                    } else {
                        if (crs != null) {
                            bw.write("C�digo: " + cod + "\tNombre: " + result.getString("COORD_REF_SYS_NAME") + "\n");
                            bw.write("Cadena WKT: " + crs.getWKT() + "\n");
                            bw.write("Cadena Proj4: " + crs.getProj4String() + "\n");
                            bw.write("Fallo: Creado con crs antiguo \n");
                            bw.write("\n\n");
                        } else {
                            bw.write("C�digo: " + cod + "\tNombre: " + result.getString("COORD_REF_SYS_NAME") + "\n");
                            bw.write("Fallo: Falla tanto geotools como crs antiguo \n");
                            bw.write("\n\n");
                        }
                    }
                } catch (CrsException e) {
                    bw.write("C�digo: " + cod + "\tNombre: " + result.getString("COORD_REF_SYS_NAME") + "\n");
                    bw.write("Cadena WKT: No cadena wkt \n");
                    bw.write("Cadena Proj4: No cadena proj4\n");
                    bw.write("Fallo: " + e + " \n");
                    bw.write("\n\n");
                } catch (NumberFormatException e) {
                    bw.write("C�digo: " + cod + "\tNombre: " + result.getString("COORD_REF_SYS_NAME") + "\n");
                    bw.write("Cadena WKT: No cadena wkt \n");
                    bw.write("Cadena Proj4: No cadena proj4\n");
                    bw.write("Fallo: " + e + " \n");
                    bw.write("\n\n");
                } catch (ConversionException e) {
                    bw.write("C�digo: " + cod + "\tNombre: " + result.getString("COORD_REF_SYS_NAME") + "\n");
                    bw.write("Cadena WKT: No cadena wkt \n");
                    bw.write("Cadena Proj4: No cadena proj4\n");
                    bw.write("Fallo: " + e + " \n");
                    bw.write("\n\n");
                } catch (UnformattableObjectException e) {
                }
            }
            bw.flush();
            bw.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
