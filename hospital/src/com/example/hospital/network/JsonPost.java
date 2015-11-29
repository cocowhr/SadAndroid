package com.example.hospital.network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntityHC4;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPostHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import com.example.hospital.model.MyException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by coco on 2015/5/8.
 */
public class JsonPost {
	private String url;
	private JSONObject returnjsonObject = null;

	public JsonPost(HashMap<String, String> map, String url) throws Exception {
		this.url = url;
		Post post = new Post(map);
		returnjsonObject = post.PostToServer();
		post.PostExecuteLogin(returnjsonObject);
	}

	private class Post {
		// HttpClient client = new DefaultHttpClient();
		CloseableHttpClient client = HttpClients.custom().useSystemProperties().build();
		// HttpPost httpPost = new HttpPost(url);
		HttpPostHC4 httpPost = new HttpPostHC4(url);
		JSONObject jsonObject = new JSONObject();
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		HashMap<String, String> map;

		Post(HashMap<String, String> map) {
			this.map = map;
		}

		protected JSONObject PostToServer() throws Exception {
			try {
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String) entry.getKey();
					String val = (String) entry.getValue();
					jsonObject.put(key, val);
				}
				nameValuePair.add(new BasicNameValuePair("jsonString", jsonObject.toString()));
			} catch (Exception e) {
				throw new MyException.packageException();
			}
			try {
				CloseableHttpResponse response;
				httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
				httpPost.setHeader("Accept", "application/json");
				// httpPost.setHeader("Content-type", "application/json");
				httpPost.setEntity(new UrlEncodedFormEntityHC4(nameValuePair, "UTF-8"));
				response = client.execute(httpPost);
				int a = response.getStatusLine().getStatusCode();
				StringBuilder builder = new StringBuilder();
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				for (String s = reader.readLine(); s != null; s = reader.readLine()) {
					builder.append(s);
				}
				JSONObject jsonObject1 = new JSONObject(builder.toString());
				if (response != null) {
					response.close();
				}
				client.close();
				return jsonObject1;
			} catch (Exception e) {
				throw new MyException.postException();
			}
		}

		protected void PostExecuteLogin(JSONObject jsonObject) throws Exception {
			if (jsonObject == null) {
				throw new Exception();
				// TODO:网络通信错误
			} else {
				String status = jsonObject.getString("status");
				if (status.equals("normal")) {
				} else if (status.equals("password_error")) {
					throw new MyException.passwordWrongException();
				} else {
					throw new MyException.executeException();
				}
			}
		}
	}
}
