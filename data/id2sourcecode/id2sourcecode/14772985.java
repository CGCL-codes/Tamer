    private void getPage(String fonte) {
        try {
            URL url = new URL(fonte);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Request-Method", "GET");
            connection.setDoInput(true);
            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), TextUtil.ISO88591));
            StringBuffer newData = new StringBuffer(10000);
            String s = "";
            while (null != ((s = br.readLine()))) {
                newData.append(s + "\n");
            }
            br.close();
            this.extract(newData.toString());
        } catch (MalformedURLException e) {
            System.err.println("Erro na URL");
            e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Erro de I/O");
            e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro geral");
            e.printStackTrace();
            e.getMessage();
        }
    }
