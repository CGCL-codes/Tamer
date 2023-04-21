    public static long getFileSize(String path) throws Exception {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            Pattern p = Pattern.compile("(https*)://(\\S+):(\\S+)@(\\S+)");
            Matcher m = p.matcher(path);
            boolean result = m.find();
            String protocol = null;
            String user = null;
            String pass = null;
            String URL = path;
            if (result) {
                protocol = m.group(1);
                user = m.group(2);
                pass = m.group(3);
                URL = protocol + "://" + m.group(4);
            }
            URL urlObj = null;
            try {
                urlObj = new URL(URL);
                URLConnection urlConn = urlObj.openConnection();
                if (user != null && pass != null) {
                    String userPassword = user + ":" + pass;
                    String encoding = Base64.encodeBase64String(userPassword.getBytes());
                    urlConn.setRequestProperty("Authorization", "Basic " + encoding);
                }
                return urlConn.getContentLength();
            } catch (MalformedURLException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                throw e;
            } catch (IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
        if (path.startsWith("s3://")) {
            String accessKey = null;
            String secretKey = null;
            Pattern p = Pattern.compile("s3://(\\S+):(\\S+)@(\\S+)");
            Matcher m = p.matcher(path);
            boolean result = m.find();
            String URL = path;
            if (result) {
                accessKey = m.group(1);
                secretKey = m.group(2);
                URL = "s3://" + m.group(3);
            } else {
                try {
                    HashMap<String, String> settings = (HashMap<String, String>) ConfigTools.getSettings();
                    accessKey = settings.get("AWS_ACCESS_KEY");
                    secretKey = settings.get("AWS_SECRET_KEY");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
            AmazonS3Client s3 = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
            p = Pattern.compile("s3://([^/]+)/(\\S+)");
            m = p.matcher(URL);
            result = m.find();
            if (result) {
                String bucket = m.group(1);
                String key = m.group(2);
                try {
                    GetObjectRequest gor = new GetObjectRequest(bucket, key);
                    return s3.getObject(gor).getObjectMetadata().getContentLength();
                } catch (AmazonServiceException e) {
                    e.printStackTrace();
                    throw e;
                } catch (AmazonClientException e) {
                    e.printStackTrace();
                    throw e;
                }
            } else {
                return 0;
            }
        } else {
            File file = new File(path);
            if (!file.exists()) {
                throw new IllegalStateException("File not exist " + path);
            }
            return file.length();
        }
    }
