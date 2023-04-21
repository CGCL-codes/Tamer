    public File generateZipImpl() {
        String sql = "";
        ResultSet rst = null;
        Connection conn = sqlServerData.connectSQL();
        Statement stmt = null;
        try {
            String[] zipFileList = new String[10];
            int zipFileOrder = 0;
            File dir = null;
            File arquivo = null;
            BufferedWriter linha = null;
            GregorianCalendar actualDate = new GregorianCalendar();
            dir = File.createTempFile("gtransitfeed", ".zip");
            dir.delete();
            dir.mkdirs();
            System.out.println("GERANDO ZIP EM:" + dir);
            String nomeArquivo;
            nomeArquivo = "agency.txt";
            arquivo = new File(dir, nomeArquivo);
            arquivo.createNewFile();
            linha = new BufferedWriter(new FileWriter(arquivo, true));
            linha.write("agency_id, agency_name, agency_url, " + "agency_timezone, agency_lang");
            sql = "SELECT A.agency_id, A.agency_name, A.agency_url, " + " (SELECT T.timezone_name FROM projetogtf.timezone T " + " WHERE T.timezone_id = A.agency_timezone), " + " (SELECT L.lang_iso " + " FROM projetogtf.language L " + " WHERE L.lang_id = A.agency_lang) " + " FROM projetogtf.agency A";
            stmt = conn.createStatement();
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                linha.write(13);
                linha.write(10);
                linha.write(rst.getObject(1) + ",");
                linha.write(rst.getObject(2) + ",");
                linha.write(rst.getObject(3) + ",");
                linha.write(rst.getObject(4) + ",");
                linha.write(rst.getObject(5) + ",");
            }
            linha.close();
            zipFileList[zipFileOrder] = nomeArquivo;
            zipFileOrder++;
            nomeArquivo = "stops.txt";
            arquivo = new File(dir, nomeArquivo);
            arquivo.createNewFile();
            linha = new BufferedWriter(new FileWriter(arquivo, true));
            linha.write("stop_id, stop_name, stop_desc" + "stop_lat, stop_lon, zone_id, stop_url");
            sql = "SELECT S.stop_id, S.stop_name, S.stop_desc, " + " S.stop_lat, S.stop_lon, S.zone_id, S.stop_url " + " FROM projetogtf.stops S";
            stmt = conn.createStatement();
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                linha.write(13);
                linha.write(10);
                linha.write(rst.getObject(1) + ",");
                linha.write(rst.getObject(2) + ",");
                linha.write(rst.getObject(3) + ",");
                linha.write(rst.getObject(4) + ",");
                linha.write(rst.getObject(5) + ",");
                linha.write(rst.getObject(6) + ",");
                linha.write(rst.getObject(7) + ",");
            }
            linha.close();
            zipFileList[zipFileOrder] = nomeArquivo;
            zipFileOrder++;
            nomeArquivo = "routes.txt";
            arquivo = new File(dir, nomeArquivo);
            arquivo.createNewFile();
            linha = new BufferedWriter(new FileWriter(arquivo, true));
            linha.write("route_id, agency_id, route_short_name, " + "route_long_name, route_desc, route_type, " + "route_url, route_color, route_text_color");
            sql = "SELECT R.route_id, R.agency_id, R.route_short_name, " + " R.route_long_name, R.route_desc, R.route_type," + " R.route_url, R.route_color, R.route_text_color " + " FROM projetogtf.routes R ";
            stmt = conn.createStatement();
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                linha.write(13);
                linha.write(10);
                linha.write(rst.getObject(1) + ",");
                linha.write(rst.getObject(2) + ",");
                linha.write(rst.getObject(3) + ",");
                linha.write(rst.getObject(4) + ",");
                linha.write(rst.getObject(5) + ",");
                linha.write(rst.getObject(6) + ",");
                linha.write(rst.getObject(7) + ",");
                linha.write(rst.getObject(8) + ",");
                linha.write(rst.getObject(9) + ",");
            }
            linha.close();
            zipFileList[zipFileOrder] = nomeArquivo;
            zipFileOrder++;
            nomeArquivo = "trips.txt";
            arquivo = new File(dir, nomeArquivo);
            arquivo.createNewFile();
            linha = new BufferedWriter(new FileWriter(arquivo, true));
            linha.write("route_id, service_id, trip_id ");
            sql = "SELECT T.route_id, T.service_id, T.trip_id " + "FROM projetogtf.trips T ";
            stmt = conn.createStatement();
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                linha.write(13);
                linha.write(10);
                linha.write(rst.getObject(1) + ",");
                linha.write(rst.getObject(2) + ",");
                linha.write(rst.getObject(3) + ",");
            }
            linha.close();
            linha.close();
            zipFileList[zipFileOrder] = nomeArquivo;
            zipFileOrder++;
            nomeArquivo = "stop_times.txt";
            arquivo = new File(dir, nomeArquivo);
            arquivo.createNewFile();
            linha = new BufferedWriter(new FileWriter(arquivo, true));
            linha.write("trip_id, arrival_time, stop_id");
            sql = "SELECT S.trip_id, S.time, S.stop_id " + " FROM projetogtf.stop_times S ";
            stmt = conn.createStatement();
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                linha.write(13);
                linha.write(10);
                linha.write(rst.getObject(1) + ",");
                linha.write(rst.getObject(2) + ",");
                linha.write(rst.getObject(3) + ",");
            }
            linha.close();
            zipFileList[zipFileOrder] = nomeArquivo;
            zipFileOrder++;
            nomeArquivo = "calendar.txt";
            arquivo = new File(dir, nomeArquivo);
            arquivo.createNewFile();
            linha = new BufferedWriter(new FileWriter(arquivo, true));
            linha.write("service_id, monday, tuesday, wednesday, " + "thursday, friday, saturday, " + "sunday, start_date, end_date");
            sql = "SELECT C.service_id, C.monday, C.tuesday, C.wednesday, " + "C.thursday, C.friday, C.saturday, C.sunday, C.start_date, C.end_date " + "FROM projetogtf.calendar C ";
            stmt = conn.createStatement();
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                linha.write(13);
                linha.write(10);
                linha.write(rst.getObject(1) + ",");
                linha.write(rst.getObject(2) + ",");
                linha.write(rst.getObject(3) + ",");
                linha.write(rst.getObject(4) + ",");
                linha.write(rst.getObject(5) + ",");
                linha.write(rst.getObject(6) + ",");
                linha.write(rst.getObject(7) + ",");
                linha.write(rst.getObject(8) + ",");
                linha.write(rst.getObject(9) + ",");
                linha.write(rst.getObject(10) + ",");
            }
            linha.close();
            zipFileList[zipFileOrder] = nomeArquivo;
            zipFileOrder++;
            nomeArquivo = "calendar_dates.txt";
            arquivo = new File(dir, nomeArquivo);
            arquivo.createNewFile();
            linha = new BufferedWriter(new FileWriter(arquivo, true));
            linha.write("service_id, date, exception_type");
            sql = "SELECT CD.exception_id, CD.date, CD.exception_type " + "FROM projetogtf.calendar_dates CD ";
            stmt = conn.createStatement();
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                linha.write(13);
                linha.write(10);
                linha.write(rst.getObject(1) + ",");
                linha.write(rst.getObject(2) + ",");
                linha.write(rst.getObject(3) + ",");
            }
            linha.close();
            zipFileList[zipFileOrder] = nomeArquivo;
            zipFileOrder++;
            nomeArquivo = "fare_attributes.txt";
            arquivo = new File(dir, nomeArquivo);
            arquivo.createNewFile();
            linha = new BufferedWriter(new FileWriter(arquivo, true));
            linha.write("fare_id, price, currency_type, " + "payment_method, transfers, transfer_duration");
            sql = "SELECT FA.fare_id, FA.price, FA.currency_type, " + "(SELECT CT.currency_code FROM projetogtf.currency_type CT " + " WHERE CT.currency_id = FA.currency_type), " + "FA.transfers, FA.transfer_duration " + "FROM projetogtf.fare_attributes FA ";
            stmt = conn.createStatement();
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                linha.write(13);
                linha.write(10);
                linha.write(rst.getObject(1) + ",");
                linha.write(rst.getObject(2) + ",");
                linha.write(rst.getObject(3) + ",");
                linha.write(rst.getObject(4) + ",");
                linha.write(rst.getObject(5) + ",");
                linha.write(rst.getObject(6) + ",");
            }
            linha.close();
            zipFileList[zipFileOrder] = nomeArquivo;
            zipFileOrder++;
            nomeArquivo = "fare_rules.txt";
            arquivo = new File(dir, nomeArquivo);
            arquivo.createNewFile();
            linha = new BufferedWriter(new FileWriter(arquivo, true));
            linha.write("fare_id, route_id, origin_id, " + "destination_id, contains_id");
            sql = "SELECT FR.fare_id, FR.route_id, " + "FR.origin_id, FR.destination_id, FR.contains_id " + "FROM projetogtf.fare_rules FR ";
            stmt = conn.createStatement();
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                linha.write(13);
                linha.write(10);
                linha.write(rst.getObject(1) + ",");
                linha.write(rst.getObject(2) + ",");
                linha.write(rst.getObject(3) + ",");
                linha.write(rst.getObject(4) + ",");
                linha.write(rst.getObject(5) + ",");
            }
            linha.close();
            zipFileList[zipFileOrder] = nomeArquivo;
            zipFileOrder++;
            nomeArquivo = "shapes.txt";
            arquivo = new File(dir, nomeArquivo);
            arquivo.createNewFile();
            linha = new BufferedWriter(new FileWriter(arquivo, true));
            linha.write("shape_id, shape_pt_lat, shape_pt_lon, " + "shape_pt_sequence, shape_dist_traveled");
            sql = "SELECT SP.route_id, SP.shape_pt_sequence, " + "SP.shape_pt_lat, SP.shape_pt_lon, SP.shape_dist_traveled " + "FROM projetogtf.shape_point SP ";
            stmt = conn.createStatement();
            rst = stmt.executeQuery(sql);
            while (rst.next()) {
                linha.write(13);
                linha.write(10);
                linha.write(rst.getObject(1) + "" + rst.getObject(2) + ",");
                linha.write(rst.getObject(3) + ",");
                linha.write(rst.getObject(4) + ",");
                linha.write(rst.getObject(2) + ",");
                linha.write(rst.getObject(5) + ",");
            }
            linha.close();
            zipFileList[zipFileOrder] = nomeArquivo;
            zipFileOrder++;
            byte[] buf = new byte[1024];
            String outFilename = "google_transit.zip";
            File outFile = new File(dir, outFilename);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFile));
            for (int i = 0; i < zipFileList.length; i++) {
                FileInputStream in = new FileInputStream(new File(dir, zipFileList[i]));
                out.putNextEntry(new ZipEntry(zipFileList[i]));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            return outFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("SQLException: " + e.getMessage());
                }
            }
        }
    }
