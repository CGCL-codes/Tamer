	public void getLocation(final Handler handler) {

		// 获取经纬度
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Criteria criteria = new Criteria();
			// 获得最好的定位效果
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			// 使用省电模式
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			// 获得当前位置提供者
			String provider = locationManager.getBestProvider(criteria, true);
			// 注册GPS监听器
			if (gpslistener == null) {
				gpslistener = new GpsListener(handler);
			}
			locationManager.requestLocationUpdates(provider, 2000, 10, gpslistener);

			run = new Thread() {
				@Override
				public void run() {
					// if (Cons.GPS_LAT_DATA.equals("") &&
					// Cons.GPS_LON_DATA.equals("") && lat.equals("")
					// && lon.equals("")) {
					unRequestListener();
					handler.sendEmptyMessage(Cons.STOP_GPS);
					// }
				}
			};

			myHandler.postDelayed(run, 2000 * 10);

			// 基站定位
			run2 = new Thread() {
				@Override
				public void run() {
					try {
						if (gpslistener != null) {
							TelephonyManager tm = (TelephonyManager) context
									.getSystemService(Context.TELEPHONY_SERVICE);
							GsmCellLocation gcl = (GsmCellLocation) tm.getCellLocation();
							int cid = gcl.getCid();
							int lac = gcl.getLac();
							System.out.println("cid:" + cid + "  lac:" + lac);

							int mcc = Integer.valueOf(tm.getNetworkOperator().substring(0, 3));
							int mnc = Integer.valueOf(tm.getNetworkOperator().substring(3, 5));

							// 组装JSON查询字符串
							JSONObject holder = new JSONObject();
							holder.put("version", "1.1.0");
							holder.put("host", "maps.google.com");
							// holder.put("address_language", "zh_CN");
							holder.put("request_address", true);

							JSONArray array = new JSONArray();
							JSONObject data = new JSONObject();
							data.put("cell_id", cid); // 25070
							data.put("location_area_code", lac);// 4474
							data.put("mobile_country_code", mcc);// 460
							data.put("mobile_network_code", mnc);// 0
							array.put(data);
							holder.put("cell_towers", array);
							// 创建连接，发送请求并接受回应
							DefaultHttpClient client = new DefaultHttpClient();
							HttpPost post = new HttpPost("http://www.google.com/loc/json");
							StringEntity se = new StringEntity(holder.toString());

							post.setEntity(se);
							HttpResponse resp = client.execute(post);
							HttpEntity entity = resp.getEntity();
							BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
							StringBuffer sb = new StringBuffer();
							String result = br.readLine();

							while (result != null) {
								sb.append(result);
								result = br.readLine();
							}
							JSONObject json = new JSONObject(sb.toString());
							JSONObject jsonstr = new JSONObject(json.getString("location"));
							lat = jsonstr.getString("latitude");
							lon = jsonstr.getString("longitude");
						}
					} catch (Exception e) {
						e.printStackTrace();
						handler.sendEmptyMessage(Cons.GPS_Error);
					}

				}
			};
			myHandler.post(run2);

			run3 = new Thread() {
				@Override
				public void run() {
					if (!lat.equals("") && !lon.equals("")) {
						if (gpslistener != null) {
							Message msg = new Message();
							if (lat != null && !lat.equals("") && lon != null && !lon.equals("")) {
								msg.what = Cons.GPS_LOCATION_CHANGE;
								Bundle bundle = new Bundle();
								bundle.putDouble(Cons.GPS_LAT, Double.parseDouble(lat));
								bundle.putDouble(Cons.GPS_LONG, Double.parseDouble(lon));
								Cons.GPS_LAT_DATA = String.valueOf(lat);
								Cons.GPS_LON_DATA = String.valueOf(lon);
								msg.setData(bundle);
								handler.sendMessage(msg);
							} else {
								msg.what = Cons.STOP_GPS;
							}
						}
					}
				}

			};
			// 五秒后执行基站定位的数据发送
			myHandler.postDelayed(run3, 5 * 1000);

		} else {
			handler.sendEmptyMessage(Cons.NO_GPS);
		}
	}
