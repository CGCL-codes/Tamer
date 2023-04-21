    public void run() {
        try {
            log.debug("run START, getting properties credentials");
            ClassLoader loader = AWSDataTransfer.class.getClassLoader();
            if (loader == null) loader = ClassLoader.getSystemClassLoader();
            String propFile = "AwsCredentials.properties";
            java.net.URL url = loader.getResource(propFile);
            PropertiesCredentials props = new PropertiesCredentials(url.openStream());
            log.debug("run START, properties credentials keeped");
            AmazonS3 s3 = new AmazonS3Client(props);
            log.debug("run AmazonS3 connection keeped");
            String key = fileToTransfer.getName();
            log.debug("===========================================");
            log.debug("Getting Started with Amazon S3");
            log.debug("===========================================\n");
            boolean success = fileToTransfer.renameTo(new File(tmpTransferringDir, fileToTransfer.getName()));
            File newFileToTransfer = null;
            log.debug("file moved into tmpTransferringDir: " + tmpTransferringDir);
            if (success) {
                DataListProviderProxy dlProviderProxy = new DataListProviderProxy();
                dlProviderProxy.updateContentChargingState(customerId, fileToTransfer.getName(), Constants.TRASFERRING_TO_AMAZON);
                newFileToTransfer = new File(tmpTransferringDir + PropertiesManager.getInstance().getApplicationProperties("current.separator") + fileToTransfer.getName());
                log.debug("Try to get file: " + newFileToTransfer.getAbsolutePath());
                log.info("Uploading a new object to S3 from a file\n");
                s3.putObject(new PutObjectRequest(bucketName, key, newFileToTransfer));
                log.info(key + " file successfully uploaded");
                success = newFileToTransfer.renameTo(new File(tmpTransferringDir + PropertiesManager.getInstance().getApplicationProperties("current.separator") + Constants.TRANSFERRED_DIR_NAME, newFileToTransfer.getName()));
                if (success) {
                    log.info(key + " file successfully uploaded on AmazonS3 and moved into " + tmpTransferringDir + PropertiesManager.getInstance().getApplicationProperties("current.separator") + Constants.TRANSFERRED_DIR_NAME + " directory.");
                    dlProviderProxy.updateContentChargingState(customerId, fileToTransfer.getName(), Constants.COMPLETELY_TRASFERED_TO_AMAZON);
                }
            } else {
                log.warn("WARNING. ERROR in moving file. Check permissions and file owner.");
            }
        } catch (AmazonServiceException ase) {
            log.error("Caught an AmazonServiceException, which means your request made it " + "to Amazon S3, but was rejected with an error response for some reason.");
            log.error("Error Message:    " + ase.getMessage());
            log.error("HTTP Status Code: " + ase.getStatusCode());
            log.error("AWS Error Code:   " + ase.getErrorCode());
            log.error("Error Type:       " + ase.getErrorType());
            log.error("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            log.error("Caught an AmazonClientException, which means the client encountered " + "a serious internal problem while trying to communicate with S3, " + "such as not being able to access the network.");
            log.error("Error Message: " + ace.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
