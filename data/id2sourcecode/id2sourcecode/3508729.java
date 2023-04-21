        public void download(URL url, MapInfo map_info) {
            try {
                URLConnection connection = url.openConnection();
                if (resources_.getBoolean(KEY_HTTP_PROXY_AUTHENTICATION_USE)) {
                    String proxy_userid = resources_.getString(KEY_HTTP_PROXY_AUTHENTICATION_USERNAME);
                    String proxy_password = resources_.getString(KEY_HTTP_PROXY_AUTHENTICATION_PASSWORD);
                    String auth_string = proxy_userid + ":" + proxy_password;
                    auth_string = "Basic " + new sun.misc.BASE64Encoder().encode(auth_string.getBytes());
                    connection.setRequestProperty("Proxy-Authorization", auth_string);
                }
                connection.connect();
                String mime_type = connection.getContentType().toLowerCase();
                if (!mime_type.startsWith("image")) {
                    throw new IOException("Invalid mime type (expected 'image/*'): " + connection.getContentType());
                }
                int content_length = connection.getContentLength();
                if (content_length < 0) progress_bar_bytes_.setIndeterminate(true); else progress_bar_bytes_.setMaximum(content_length);
                String extension = mime_type.substring(mime_type.indexOf('/') + 1);
                String dirname = map_info.getFilename();
                String filename = FileUtil.getNextFileName(dirname, MAP_PREFIX, MAP_PATTERN, "." + extension);
                map_info.setFilename(filename);
                FileOutputStream out = new FileOutputStream(filename);
                byte[] buffer = new byte[BUFFER_SIZE];
                BufferedInputStream in = new BufferedInputStream(connection.getInputStream(), BUFFER_SIZE);
                int sum_bytes = 0;
                int num_bytes = 0;
                while ((num_bytes = in.read(buffer)) != -1) {
                    out.write(buffer, 0, num_bytes);
                    sum_bytes += num_bytes;
                    progress_bar_bytes_.setValue(sum_bytes);
                }
                progress_bar_bytes_.setIndeterminate(false);
                in.close();
                out.close();
                downloadTerminated(map_info, DOWNLOAD_SUCCESS, sum_bytes + " " + resources_.getString(KEY_LOCALIZE_BYTES_READ));
            } catch (Exception e) {
                downloadTerminated(map_info, DOWNLOAD_ERROR, e.getMessage());
            }
        }
