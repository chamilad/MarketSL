package lk.ucsc.marketsl;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EnterNumber extends Activity {

	EditText number;
	Button send_button;
	TextView display_msg;
	String msg;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_number);

		number = (EditText) findViewById(R.id.number);
		display_msg = (TextView)findViewById(R.id.display_msg);
		
		Bundle extras = getIntent().getExtras();
		msg = extras.getString("msg");
		display_msg.setText(msg);

	}

	public void onSendButtonClick(View v) {
		String number = this.number.getText().toString();

		

		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
				EnterNumber.class), 0);
		SmsManager sms = SmsManager.getDefault();
		System.out.println("msg to send : " + msg);
		//gives an IllegalStateException here
		sms.sendTextMessage(number, null, msg, pi, null);

		Toast.makeText(getBaseContext(), "SMS Sent.", Toast.LENGTH_SHORT)
				.show();
		finish();
	}

}
