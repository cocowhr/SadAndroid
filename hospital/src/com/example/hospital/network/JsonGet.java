package com.example.hospital.network;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGetHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import com.example.hospital.model.MyException;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by coco on 2015/6/3.
 */
public class JsonGet {
	private String url;

	private class Get {
		CloseableHttpClient client = HttpClients.custom().useSystemProperties().build();
		HttpGetHC4 httpget = new HttpGetHC4(url);

		protected JSONObject GetFromServer() throws Exception {
			try {
				CloseableHttpResponse response = null;
				httpget.setHeader("Content-Type", "application/x-www-form-urlencoded");
				httpget.setHeader("Accept", "application/json");
				// httpget.setHeader("Content-type", "application/json");
				response = client.execute(httpget);
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
				throw new MyException.getException();
			}
		}
	}
}
