package com.softark.eddie.gasexpress.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SmsHelper extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Bundle bundle = intent.getExtras();
//        SmsMessage[] smsMessages;
//        String str = "";
//        if(bundle != null) {
//            Object[] pdu = (Object[]) bundle.get("pdus");
//            smsMessages = new SmsMessage[pdu.length];
//            for (int i=0; i<smsMessages.length; i++){
//                smsMessages[i] = SmsMessage .createFromPdu((byte[])pdu[i]);
//                str += "SMS from " + smsMessages[i].getOriginatingAddress();
//                str += " :";
//                str += smsMessages[i].getMessageBody();
//                str += "n";
//            }
//            Toast.makeText(context, str, Toast.LENGTH_LONG).show();
//        }
    }
}
