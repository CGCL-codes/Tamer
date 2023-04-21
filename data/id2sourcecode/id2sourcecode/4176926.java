    private void _extractFileFromJar(java.util.jar.JarFile i_jarFile, String i_fileName) throws java.io.IOException {
        java.io.InputStream inputStream = i_jarFile.getInputStream(i_jarFile.getEntry(i_fileName));
        java.io.BufferedReader bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream));
        java.io.OutputStream outputStream = new java.io.FileOutputStream(_installDirectory + "/" + i_fileName);
        java.io.BufferedWriter bufferedWriter = new java.io.BufferedWriter(new java.io.OutputStreamWriter(outputStream));
        String nextLine = bufferedReader.readLine();
        while (nextLine != null) {
            bufferedWriter.write(nextLine + '\n');
            nextLine = bufferedReader.readLine();
        }
        bufferedReader.close();
        bufferedWriter.close();
        inputStream.close();
        outputStream.close();
    }
