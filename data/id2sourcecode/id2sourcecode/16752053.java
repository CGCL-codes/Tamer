    public Object execute(Object... parameters) throws JsonRpcException {
        try {
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("method", name);
            values.put("params", parameters);
            values.put("id", "" + gid++);
            String content = null;
            ;
            try {
                JSONWriter writer = new JSONValidatingWriter(new ExceptionErrorListener());
                content = writer.write(values);
            } catch (NullPointerException e) {
                throw new JsonRpcException("cannot encode object to json", e);
            }
            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("method", "POST");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStream out = connection.getOutputStream();
            out.write(content.getBytes("utf-8"));
            out.close();
            connection.connect();
            InputStream in = connection.getInputStream();
            BufferedReader i = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = i.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            in.close();
            Map<String, Object> result = null;
            try {
                JSONReader reader = new JSONValidatingReader(new ExceptionErrorListener());
                result = (Map<String, Object>) reader.read(sb.toString());
            } catch (Exception e) {
                throw new JsonRpcException("cannot decode json", e);
            }
            if (result.get("error") != null) {
                throw new JsonRpcException(result.get("error").toString());
            }
            return result.get("result");
        } catch (JsonRpcException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new JsonRpcException("shit");
        }
    }
