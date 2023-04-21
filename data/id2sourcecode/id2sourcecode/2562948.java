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
