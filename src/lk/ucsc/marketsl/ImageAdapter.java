package lk.ucsc.marketsl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;

	public ImageAdapter(Context c) {
		mContext = c;
	}

	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
			// attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(55, 55));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(2, 2, 2, 2);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(mThumbIds[position]);
		imageView.setTag(mNames[position]);
		//System.out.println("Tag: " + (String)imageView.getTag());
		return imageView;
	}

	// references to our images
	private Integer[] mThumbIds = { R.drawable.ambul, R.drawable.arecanut,
			R.drawable.ash_plantain, R.drawable.beans,
			R.drawable.big_onion_imp, R.drawable.brinjal, R.drawable.batana,
			R.drawable.bitter_gourd, R.drawable.beetroot, R.drawable.cabbage,
			R.drawable.cardamon, R.drawable.coffee, R.drawable.cinnamon,
			R.drawable.cloves, R.drawable.coconut, R.drawable.capsicum,
			R.drawable.carrot, R.drawable.cucumber, R.drawable.cowpea,
			R.drawable.dried_chili, R.drawable.elabatu, R.drawable.garcinia,
			R.drawable.gingeli, R.drawable.ginger, R.drawable.green_chili,
			R.drawable.gram_green, R.drawable.icon, R.drawable.kakeri,
			R.drawable.kurakkan, R.drawable.icon, R.drawable.kolikuttu,
			R.drawable.icon, R.drawable.karunka, R.drawable.kithul_honey,
			R.drawable.kitul_treacle, R.drawable.long_beans, R.drawable.luffa,
			R.drawable.leeks, R.drawable.lime, R.drawable.maize,
			R.drawable.murunga, R.drawable.nadu_rice, R.drawable.okra,
			R.drawable.papaya, R.drawable.pumpkin, R.drawable.pepper,
			R.drawable.potato, R.drawable.radish, R.drawable.red_onion_imp,
			R.drawable.samba_rice, R.drawable.snake_gourd,
			R.drawable.seeni_kesel, R.drawable.sweet_potato,
			R.drawable.soy_beans, R.drawable.thibbatu, R.drawable.tumeric,
			R.drawable.icon, R.drawable.tomato, R.drawable.wing_bean };

	private String[] mNames = { "Ambul Kesel", "Arecanut", "Ash-Plantain",
			"Beans", "Big Onion", "Brinjal", "Batana", "Bitter Gourd",
			"Beetroot", "Cabbage", "Cardamom", "Coffee", "Cinnamon", "Cloves",
			"Coconut", "Capsicum", "Carrot", "Cucumber", "Cowpea",
			"Dried Chili", "Elabatu", "Garcinia", "Gingeli", "Ginger",
			"Green chili", "Green Gram", "Gotukola", "Kakeri", "Kurakkan",
			"Kekulu Rice", "Kolikuttu", "Knolkhol", "Karunka", "Kithul Honey",
			"Kitul Treacle", "Long Bean", "Luffa", "Leeks", "Lime", "Maize",
			"Murunga", "Nadu Rice", "Okra", "Papaya", "Pumpkin", "Pepper",
			"Potato", "Radish", "Red Onion", "Samba Rice", "Snake Gourd",
			"Seeni Kesel", "Sweet Potato", "Soya Beans", "Thibbatu", "Tumeric",
			"Tamarind", "Tomato", "Wing Bean" };
	
	//private String[] mNames2 = getResources().getStringArray(R.array.crops_array);
}