    public static void main(String[] args) {
        String gUsername = null;
        String authTokenPicasa = null;
        String imgUri = null;
        String dropboxUrl = "http://picasaweb.google.com/data/feed/api/user/" + gUsername + "/albumid/default";
        HttpURLConnection uc;
        try {
            uc = (HttpURLConnection) new URL(dropboxUrl).openConnection();
            uc.setDoOutput(true);
            uc.setUseCaches(false);
            uc.setRequestMethod("POST");
            uc.setRequestProperty("GData-Version", "2");
            uc.setRequestProperty("Authorization", "GoogleLogin auth=" + authTokenPicasa);
            uc.setRequestProperty("Content-Type", "image/jpeg");
            DataOutputStream dataOutput = new DataOutputStream(uc.getOutputStream());
            DataInputStream dis = null;
            try {
                dis = new DataInputStream(new FileInputStream(imgUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int read = 0;
            while ((read = dis.read()) != -1) {
                dataOutput.write(read);
            }
            dataOutput.flush();
            dis.close();
            dataOutput.close();
            if (uc.getResponseCode() == HttpsURLConnection.HTTP_CREATED) {
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
