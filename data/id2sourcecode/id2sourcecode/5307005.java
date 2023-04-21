    public void print() throws IORuntimeException {
        BufferedReader bis = new BufferedReader(new InputStreamReader(serverIn));
        String line;
        try {
            print.printf(Locale.US, "%s%s\n", getCode(), queue);
            while ((line = bis.readLine()) != null) response.write(line + "\n");
            response.flush();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
