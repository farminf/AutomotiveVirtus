package com.xmpp_android.activities;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.xmpp_android.R;
import com.xmpp_android.settings.About;
import com.xmpp_android.settings.Connection_Setting;
import com.xmpp_android.xmpp.XMPP;
import com.xmpp_android.xmpp.XMPPService;

public class MainActivity extends Activity {

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction("show");
		registerReceiver(broadcastReceiver, filter);
		 Log.d("info", "Broadcast registered");

		// mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		// IntentFilter filter = new IntentFilter();
		// //filter.addAction("show");
		// mLocalBroadcastManager.registerReceiver(broadcastReceiver, filter);
		
	}

	SharedPreferences sharedPref;
	String username;
	String password;
	String serveraddress;
	int serverport;
	XMPP openFireConnection;
	Button btnconnectToServer;
	Button btnSendMessage;
	Button btnDisconnectFromServer;
	Button btnAddRoster;
	Button btnStartService;
	Button btnCreatePubSubNode;
	Button btnPublishToNode;
	Button btnSubscribeToNode;
	Button btnSendAdhoc;
	String AddressedUser;
	String domain;
	String SendMessage;
	String rosterUsernameToAdd;
	String rosterNickNameToAdd;
	String adhocUsernameToSend;
	String adhocCommandToSend;

	BroadcastReceiver broadcastReceiver  = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// do something based on the intent's action
			Log.d("info", "Broadcast received");
		}
	};
	LocalBroadcastManager mLocalBroadcastManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		domain = getString(R.string.domain_name);

		// Getting User and Server Configuration from Setting shared Preferences
		getSharedPreference();

		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// do something based on the intent's action
				Log.d("info", "Broadcast received");
			}
		};

		// // BroadCast Receiver
		// mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		// broadcastReceiver = new BroadcastReceiver() {
		// @Override
		// public void onReceive(Context context, Intent intent) {
		//
		// Log.d("info", "Broadcast received-1");
		// if (intent.getAction().equals("ShowProgressBar")) {
		// // do work here
		// Toast.makeText(getApplicationContext(),
		// "Activity Received the BroadCast MSG", Toast.LENGTH_LONG)
		// .show();
		// Log.d("info", "Broadcast received");
		//
		// }
		// }
		// };
		
		
		
		// Button to Start the Service
		btnStartService = (Button) findViewById(R.id.btnStartService);
		btnStartService.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Start the Service
				Intent serviceIntent = new Intent(getBaseContext(),
						XMPPService.class);
				startService(serviceIntent);
			}
		});

		// button Disconnect and it's listener
		btnDisconnectFromServer = (Button) findViewById(R.id.btnDisconnectFromServer);
		btnDisconnectFromServer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				Intent serviceIntent = new Intent(getBaseContext(),
//						XMPPService.class);
//				stopService(serviceIntent);
				
				//BroadCast Manager
				 mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
				//Broadcast Sends to Activity
				Intent broadcastIntent = new Intent();
				broadcastIntent.setAction("show");
			   // broadcastIntent.putExtra(whateverExtraData you need to pass back);
				sendBroadcast(broadcastIntent);
				Log.d("sender", "Broadcasting message");
				
			}
		});

		// button Send and it's listener
		btnSendMessage = (Button) findViewById(R.id.btnsendmsg);
		btnSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddressedUser = "android2";
				try {
					SendMessage = "Hey man";
					XMPPService.sendMessage(AddressedUser, SendMessage);
				} catch (NotConnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("Send msg", "cannot Send A msg: " + e.getMessage());

				}
			}
		});

		// button to add roster and it's listener
		btnAddRoster = (Button) findViewById(R.id.btnAddRoster);
		btnAddRoster.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					rosterUsernameToAdd = "sender1";
					rosterNickNameToAdd = "adhoc sender";
					XMPPService.addRosterEntry(rosterUsernameToAdd,
							rosterNickNameToAdd);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("Add Roster", "cannot Add roster: " + e.getMessage());

				}
			}
		});

		// button to Create A Pub/Sub Node
		btnCreatePubSubNode = (Button) findViewById(R.id.btnCreateNode);
		btnCreatePubSubNode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String nodeNameToCreate = "testnode-1";

				try {

					XMPPService.createPubSubNode(nodeNameToCreate);

				} catch (NoResponseException | XMPPErrorException
						| NotConnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("CreateNode",
							"cannot Create A node: " + e.getMessage());

				}
			}
		});
		// button to publish to the node
		btnPublishToNode = (Button) findViewById(R.id.btnPub);
		btnPublishToNode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String nodeNameToPublish = "testnode-1";
				try {

					XMPPService.publishToNode(nodeNameToPublish);

				} catch (NoResponseException | XMPPErrorException
						| NotConnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("Publish Node",
							"cannot publish to the node: " + e.getMessage());
				}
			}
		});

		// button to subscribe to the node
		btnSubscribeToNode = (Button) findViewById(R.id.btnSub);
		btnSubscribeToNode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String nodeNameToSubscribe = "testnode-1";
				try {

					XMPPService.subscribeToNode(nodeNameToSubscribe);

				} catch (NoResponseException | XMPPErrorException
						| NotConnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("Subscribe Node", "cannot subscribe to the node: "
							+ e.getMessage());
				}
			}
		});

		// button to send Ad-Hoc Command
		btnSendAdhoc = (Button) findViewById(R.id.btnSendAdhoc);
		btnSendAdhoc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				adhocUsernameToSend = "receiver1";
				adhocCommandToSend = "first_custom_command";
				try {
					XMPPService.sendAdhocCommand(adhocUsernameToSend,
							adhocCommandToSend);
				} catch (XMPPException | SmackException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	// End Of OnCreate
	// ********************************************************************

	// function which reads parameters that has set in settings
	private void getSharedPreference() {
		// TODO Auto-generated method stub
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		username = sharedPref.getString("textUsername", "");
		password = sharedPref.getString("textPassword", "");
		serveraddress = sharedPref.getString("textServerAddress", "");
		serverport = 5222;
		Log.d("Settings", username + "," + password + "," + serveraddress + ":"
				+ serverport);
	}

	// Menu and Setting functions
	// -----------------------------------------------
	// ---------------------------------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent ConnectionSettingIntent = new Intent(this,
					Connection_Setting.class);
			startActivity(ConnectionSettingIntent);
			return true;
		case R.id.about:
			Intent aboutIntent = new Intent(this, About.class);
			startActivity(aboutIntent);
			return true;
		case R.id.exit:
			finish();
			System.exit(0);
			return true;
		default:
			break;
		}
		return false;

	}
}
