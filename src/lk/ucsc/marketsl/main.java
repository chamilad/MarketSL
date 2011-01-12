package lk.ucsc.marketsl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class main extends Activity {
	/** Called when the activity is first created. */

	String crop_name;
	String[] CROPS;
	String[] CROP_CODES;
	//TextView crop_info_display;
	//String temp_crop_name;
	//ArrayList<Crop> crops_selected;
	ListView selected_crops_list;
	static Crop[] crops_array;
	ImageView icon_image;
	private ProgressDialog progressDialog;
	String can_crops;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Bundle extras = getIntent().getExtras();
		crop_name = extras.getString("crop_name");

		CROPS = getResources().getStringArray(R.array.crops_array);

		CROP_CODES = getResources().getStringArray(R.array.crop_codes_array);
		//crop_info_display = (TextView) findViewById(R.id.crop_details_display);

		selected_crops_list = (ListView) findViewById(R.id.selected_crops_list);

		//crops_selected = new ArrayList<Crop>();

		icon_image = (ImageView) findViewById(R.id.crop_image);

		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Fetching Crop Info...");
		
		submit_query();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.sms_all:
	    	String msg = can_crops ;
	    	Intent intent = new Intent(getApplicationContext(), EnterNumber.class);
	    	intent.putExtra("msg", msg);
			startActivity(intent);
	    	
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	public void submit_query() {
		if (validCropName(this.crop_name)) {
			// get crop code for crop name
			String crop_code = getCropCode(this.crop_name);
			crops_array = getCropInfo(crop_code, this.crop_name);

		} else {
			// not a valid crop name
		}
	}

	private boolean validCropName(String crop_name) {
		// TO DO: remove spaces, make camel case
		for (String c : CROPS) {
			if (crop_name.equals(c))
				return true;
		}
		return false;
	}

	private String getCropCode(String crop_name) {
		return CROP_CODES[getIndexInArray(crop_name)];
	}

	private int getIndexInArray(String crop_name) {
		for (int i = 0; i < CROPS.length; i++) {
			if (crop_name.equals(CROPS[i])) {
				return i;
			}
		}
		return 666;
	}

	public Crop[] getCropInfo(String crop_code, String crop_name) {
		// starts the thread
		progressDialog.show();
		GetCropInfoTask getinfotask = new GetCropInfoTask();
		getinfotask.execute(crop_code, crop_name);

		return null;
	}

	class GetCropInfoTask extends AsyncTask<String, Object, Object> {

		@Override
		protected Object doInBackground(String... params) {
			String crop_code = params[0];
			Crop[] crops_array;

			String URL = "http://mobile.icta.lk/services/cropservice/getCropData.php?cropCode="
					+ crop_code;
			String response = "";

			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(URL);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");

			ResponseHandler<String> handler = new BasicResponseHandler();

			try {
				response = client.execute(request, handler);
				// System.out.println("response " + response);

				JSONArray crops_response = new JSONObject(response)
						.getJSONArray("crops");
				JSONObject crop_result = null;
				crops_array = new Crop[crops_response.length()];
				for (int i = 0; i < crops_response.length(); i++) {
					crop_result = crops_response.getJSONObject(i);
					crops_array[i] = new Crop(crop_result.getString("name"),
							crop_result.getString("location"), crop_result
									.getString("price"));
				}

				client.getConnectionManager().shutdown();
				// return crops_array;
				main.crops_array = crops_array;
				Message msg = service_handler.obtainMessage();
				Bundle bundle = new Bundle();
				bundle.putString("crop_name", params[1]);

				msg.setData(bundle);
				service_handler.sendMessage(msg);

			} catch (Exception e) {
				client.getConnectionManager().shutdown();
				e.printStackTrace();
			}

			return null;
		}

	}

	final Handler service_handler = new Handler() {
		public void handleMessage(Message message) {
			// @SuppressWarnings("unchecked")
			progressDialog.dismiss();
			show_results(message.getData().getString("crop_name"));
		}
	};

	public void show_results(String crop_name) {
		can_crops = "";
		for (int i = 0; i < crops_array.length; i++) {
			can_crops += crops_array[i].getName()
					+ " is priced " + crops_array[i].getPrice() + " at "
					+ crops_array[i].getLocation() + "\n";
		}

		System.out.println(can_crops);

		String[] from = new String[] { "crop_name", "location", "price" };
		int[] to = new int[] { R.id.crop_name_selected, R.id.location_selected,
				R.id.price_selected };

		// System.out.println("before list");

		List<HashMap<String, String>> fill_maps = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < crops_array.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("crop_name", crops_array[i].getName());
			map.put("location", crops_array[i].getLocation());
			map.put("price", crops_array[i].getPrice());
			fill_maps.add(map);
		}

		// System.out.println("before simple adapter");
		SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
				fill_maps, R.layout.selected_crop_list_item, from, to);
		// System.out.println("after simple adapter");
		selected_crops_list.setAdapter(adapter);
		selected_crops_list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
		// System.out.println("after set adapter");

		int icon = getCropIcon(crop_name);
		System.out.println("icon int: " + icon);
		icon_image.setImageResource(icon);

		// System.out.println("ambul id : " + R.drawable.ambul);

	}

	private int getCropIcon(String crop_name) {
		if (crop_name.equals("Ambul Kesel")) {
			return R.drawable.ambul;
		}
		if (crop_name.equals("Arecanut")) {
			return R.drawable.arecanut;
		}
		if (crop_name.equals("Ash-Plantain")) {
			return R.drawable.ash_plantain;
		}
		if (crop_name.equals("Beans")) {
			return R.drawable.beans;
		}
		if (crop_name.equals("Big Onion")) {
			return R.drawable.big_onion_imp;
		}
		if (crop_name.equals("Brinjal")) {
			return R.drawable.brinjal;
		}
		if (crop_name.equals("Batana")) {
			return R.drawable.batana;
		}
		if (crop_name.equals("Bitter Gourd")) {
			return R.drawable.bitter_gourd;
		}
		if (crop_name.equals("Beetroot")) {
			return R.drawable.beetroot;
		}
		if (crop_name.equals("Cabbage")) {
			return R.drawable.cabbage;
		}
		if (crop_name.equals("Cardamom")) {
			return R.drawable.cardamon;
		}
		if (crop_name.equals("Coffee")) {
			return R.drawable.coffee;
		}
		if (crop_name.equals("Cinnamon")) {
			return R.drawable.cinnamon;
		}
		if (crop_name.equals("Cloves")) {
			return R.drawable.cloves;
		}
		if (crop_name.equals("Coconut")) {
			return R.drawable.coconut;
		}
		if (crop_name.equals("Capsicum")) {
			return R.drawable.capsicum;
		}
		if (crop_name.equals("Carrot")) {
			return R.drawable.carrot;
		}
		if (crop_name.equals("Cucumber")) {
			return R.drawable.cucumber;
		}
		if (crop_name.equals("Cowpea")) {
			return R.drawable.cowpea;
		}
		if (crop_name.equals("Dried Chili")) {
			return R.drawable.dried_chili;
		}
		if (crop_name.equals("Elabatu")) {
			return R.drawable.elabatu;
		}
		if (crop_name.equals("Garcinia")) {
			return R.drawable.garcinia;
		}
		if (crop_name.equals("Gingeli")) {
			return R.drawable.gingeli;
		}
		if (crop_name.equals("Ginger")) {
			return R.drawable.ginger;
		}
		if (crop_name.equals("Green chili")) {
			return R.drawable.green_chili;
		}
		if (crop_name.equals("Green Gram")) {
			return R.drawable.gram_green;
		}
		if (crop_name.equals("Gotukola")) {
			return R.drawable.gotukola;
		}
		if (crop_name.equals("Kakeri")) {
			return R.drawable.kakeri;
		}
		if (crop_name.equals("Kurakkan")) {
			return R.drawable.kurakkan;
		}
		if (crop_name.equals("Kekulu Rice")) {
			return R.drawable.kekulu;
		}
		if (crop_name.equals("Kolikuttu")) {
			return R.drawable.kolikuttu;
		}
		if (crop_name.equals("Knolkhol")) {
			return R.drawable.knokhol;
		}
		if (crop_name.equals("Karunka")) {
			return R.drawable.karunka;
		}
		if (crop_name.equals("Kithul Honey")) {
			return R.drawable.kithul_honey;
		}
		if (crop_name.equals("Kitul Treacle")) {
			return R.drawable.kitul_treacle;
		}
		if (crop_name.equals("Long Bean")) {
			return R.drawable.long_beans;
		}
		if (crop_name.equals("Luffa")) {
			return R.drawable.luffa;
		}
		if (crop_name.equals("Leeks")) {
			return R.drawable.leeks;
		}
		if (crop_name.equals("Lime")) {
			return R.drawable.lime;
		}
		if (crop_name.equals("Maize")) {
			return R.drawable.maize;
		}
		if (crop_name.equals("Murunga")) {
			return R.drawable.murunga;
		}
		if (crop_name.equals("Nadu Rice")) {
			return R.drawable.nadu_rice;
		}
		if (crop_name.equals("Okra")) {
			return R.drawable.okra;
		}
		if (crop_name.equals("Papaya")) {
			return R.drawable.papaya;
		}
		if (crop_name.equals("Pumpkin")) {
			return R.drawable.pumpkin;
		}
		if (crop_name.equals("Pepper")) {
			return R.drawable.pepper;
		}
		if (crop_name.equals("Potato")) {
			return R.drawable.potato;
		}
		if (crop_name.equals("Radish")) {
			return R.drawable.radish;
		}
		if (crop_name.equals("Red Onion")) {
			return R.drawable.red_onion_imp;
		}
		if (crop_name.equals("Samba Rice")) {
			return R.drawable.samba_rice;
		}
		if (crop_name.equals("Snake Gourd")) {
			return R.drawable.snake_gourd;
		}
		if (crop_name.equals("Seeni Kesel")) {
			return R.drawable.seeni_kesel;
		}
		if (crop_name.equals("Sweet Potato")) {
			return R.drawable.sweet_potato;
		}
		if (crop_name.equals("Soya Beans")) {
			return R.drawable.soy_beans;
		}
		if (crop_name.equals("Thibbatu")) {
			return R.drawable.thibbatu;
		}
		if (crop_name.equals("Tumeric")) {
			return R.drawable.tumeric;
		}
		if (crop_name.equals("Tamarind")) {
			return R.drawable.tamarind;
		}
		if (crop_name.equals("Tomato")) {
			return R.drawable.tomato;
		}
		if (crop_name.equals("Wing Bean")) {
			return R.drawable.wing_bean;
		}
		return 666;
	}
}