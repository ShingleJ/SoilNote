package utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class HumanPosition {
	public static String getHumanPosition(double latitude, double longtitude) {
		String humanPosition = "";
		try {
			// ��װ����������Ľӿڵ�ַ
			StringBuilder url = new StringBuilder();
			url.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
			url.append(latitude).append(",");
			url.append(longtitude);
			url.append("&sensor=false");
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url.toString());
			// ��������Ϣͷ��ָ�����ԣ���֤�������᷵����������
			httpGet.addHeader("Accept-Language", "zh-CN");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				String response = EntityUtils.toString(entity, "utf-8");
				JSONObject jsonObject = new JSONObject(response);
				// ��ȡresults�ڵ��µ�λ����Ϣ
				JSONArray resultArray = jsonObject.getJSONArray("results");
				if (resultArray.length() > 0) {
					JSONObject subObject = resultArray.getJSONObject(0);// ȡ����еĵ�һ��
					// ȡ����ʽ�����λ����Ϣ
					humanPosition = subObject.getString("formatted_address");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return humanPosition;
	}
}
