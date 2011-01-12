package lk.ucsc.marketsl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class Home extends Activity {
	AutoCompleteTextView crop_name_ac;
	String[] CROPS;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		crop_name_ac = (AutoCompleteTextView) findViewById(R.id.autocomplete_crop);
		CROPS = getResources().getStringArray(R.array.crops_array);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.crop_list_auto_item, CROPS);
		crop_name_ac.setAdapter(adapter);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            //Toast.makeText(Home.this, "" + position + " tag : " + (String)v.getTag(), Toast.LENGTH_SHORT).show();
	        	String crop_name = (String)v.getTag();
	    		Intent intent = new Intent(getApplicationContext(), main.class);
	        	intent.putExtra("crop_name", crop_name);
	    		startActivity(intent);
	        }
	    });
	}
	
	public void submit_query(View v) {
		String crop_name = crop_name_ac.getText().toString();
		Intent intent = new Intent(getApplicationContext(), main.class);
    	intent.putExtra("crop_name", crop_name);
		startActivity(intent);
	}
	

	
}
