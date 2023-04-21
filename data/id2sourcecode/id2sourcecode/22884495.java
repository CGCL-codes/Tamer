            @Override
            public Boolean call() throws Exception {
                final HttpClient httpClient = new DefaultHttpClient();
                final HttpPost httpPost = new HttpPost(SUBMIT_URL);
                final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(18);
                nameValuePairs.add(new BasicNameValuePair("benchmark_id", String.valueOf(BaseBenchmark.this.getBenchmarkID())));
                nameValuePairs.add(new BasicNameValuePair("benchmark_versionname", BaseBenchmark.getVersionName(BaseBenchmark.this)));
                nameValuePairs.add(new BasicNameValuePair("benchmark_versioncode", String.valueOf(BaseBenchmark.getVersionCode(BaseBenchmark.this))));
                nameValuePairs.add(new BasicNameValuePair("benchmark_fps", String.valueOf(BaseBenchmark.this.mFPS).replace(",", ".")));
                nameValuePairs.add(new BasicNameValuePair("device_model", Build.MODEL));
                nameValuePairs.add(new BasicNameValuePair("device_android_version", Build.VERSION.RELEASE));
                nameValuePairs.add(new BasicNameValuePair("device_sdk_version", String.valueOf(Build.VERSION.SDK_INT)));
                nameValuePairs.add(new BasicNameValuePair("device_manufacturer", Build.MANUFACTURER));
                nameValuePairs.add(new BasicNameValuePair("device_brand", Build.BRAND));
                nameValuePairs.add(new BasicNameValuePair("device_build_id", Build.ID));
                nameValuePairs.add(new BasicNameValuePair("device_build", Build.DISPLAY));
                nameValuePairs.add(new BasicNameValuePair("device_device", Build.DEVICE));
                nameValuePairs.add(new BasicNameValuePair("device_product", Build.PRODUCT));
                nameValuePairs.add(new BasicNameValuePair("device_cpuabi", Build.CPU_ABI));
                nameValuePairs.add(new BasicNameValuePair("device_board", Build.BOARD));
                nameValuePairs.add(new BasicNameValuePair("device_fingerprint", Build.FINGERPRINT));
                nameValuePairs.add(new BasicNameValuePair("benchmark_extension_vbo", GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS ? "1" : "0"));
                nameValuePairs.add(new BasicNameValuePair("benchmark_extension_drawtexture", GLHelper.EXTENSIONS_DRAWTEXTURE ? "1" : "0"));
                final TelephonyManager telephonyManager = (TelephonyManager) BaseBenchmark.this.getSystemService(Context.TELEPHONY_SERVICE);
                nameValuePairs.add(new BasicNameValuePair("device_imei", telephonyManager.getDeviceId()));
                final DisplayMetrics displayMetrics = new DisplayMetrics();
                BaseBenchmark.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                nameValuePairs.add(new BasicNameValuePair("device_displaymetrics_widthpixels", String.valueOf(displayMetrics.widthPixels)));
                nameValuePairs.add(new BasicNameValuePair("device_displaymetrics_heightpixels", String.valueOf(displayMetrics.heightPixels)));
                nameValuePairs.add(new BasicNameValuePair("device_displaymetrics_xdpi", String.valueOf(displayMetrics.xdpi)));
                nameValuePairs.add(new BasicNameValuePair("device_displaymetrics_ydpi", String.valueOf(displayMetrics.ydpi)));
                try {
                    final float bogoMips = SystemUtils.getCPUBogoMips();
                    nameValuePairs.add(new BasicNameValuePair("device_cpuinfo_bogomips", String.valueOf(bogoMips)));
                } catch (IllegalStateException e) {
                    Debug.e(e);
                }
                try {
                    final float memoryTotal = SystemUtils.getMemoryTotal();
                    final float memoryFree = SystemUtils.getMemoryFree();
                    nameValuePairs.add(new BasicNameValuePair("device_memoryinfo_total", String.valueOf(memoryTotal)));
                    nameValuePairs.add(new BasicNameValuePair("device_memoryinfo_free", String.valueOf(memoryFree)));
                } catch (IllegalStateException e) {
                    Debug.e(e);
                }
                try {
                    final int cpuFrequencyCurrent = SystemUtils.getCPUFrequencyCurrent();
                    final int cpuFrequencyMax = SystemUtils.getCPUFrequencyMax();
                    nameValuePairs.add(new BasicNameValuePair("device_cpuinfo_frequency_current", String.valueOf(cpuFrequencyCurrent)));
                    nameValuePairs.add(new BasicNameValuePair("device_cpuinfo_frequency_max", String.valueOf(cpuFrequencyMax)));
                } catch (SystemUtilsException e) {
                    Debug.e(e);
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                final HttpResponse response = httpClient.execute(httpPost);
                final int statusCode = response.getStatusLine().getStatusCode();
                Debug.d(StreamUtils.readFully(response.getEntity().getContent()));
                if (statusCode == HttpStatus.SC_OK) {
                    return true;
                } else {
                    throw new RuntimeException();
                }
            }
