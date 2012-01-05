/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.PrimozRezek.iManager.android.Alarm;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import java.util.Calendar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * This is an example of implementing an application service that will run in
 * response to an alarm, allowing us to move long duration work out of an
 * intent receiver.
 * 
 * @see AlarmService
 * @see AlarmService_Alarm
 */
public class AlarmService_Service extends Service {
    NotificationManager mNM;
    
    @Override
    public void onCreate() 
    {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Thread thr = new Thread(null, mTask, "AlarmService_Service");
        thr.start();
        
    }

    @Override
    public void onDestroy() 
    {
        
        mNM.cancel(5432111);

    }

    /**
     * The function that runs in our worker thread
     */
    Runnable mTask = new Runnable() {  
        public void run() 
        {
        	startPlaying();

            AlarmService_Service.this.stopSelf();
        }
    };

    @Override
    public IBinder onBind(Intent intent) { 
        return mBinder;
    }

    /**
     * Show a notification while this service is running.
     */
    private void startPlaying() 
    {
    	//povemo kaj poganjamo, budilko ali odstevalnik
    	SharedPreferences bud_ali_odst = getSharedPreferences("BUDILKA_ALI_ODSTEVALNIK", 0);
        SharedPreferences.Editor editorIzbor = bud_ali_odst.edit();
        editorIzbor.putString("ali_budilka_ali_odstevalnik", "budilka");
        editorIzbor.commit();
    	
        //nastavitve nastavim da je budilka izklopljena
		SharedPreferences budilka_vklopljena = getSharedPreferences("BUDILKA_LAST_SET", 0);
		SharedPreferences.Editor editor2 = budilka_vklopljena.edit();
		editor2.putBoolean("budilka_vklopljena", false);
		editor2.commit();
    	
    	SharedPreferences zadnje_nastavitve = getSharedPreferences("NASTAVITVE_APP_LAST_SET", 0);
    	int izbor = zadnje_nastavitve.getInt("izbor_budilka", 0);
    	
    	Intent i;
    	
    	if(izbor==1) i = new Intent(this, FizicnaBudilkaActivity.class);
    	else if(izbor==2) i = new Intent(this, MatematicnaBudilkaActivity.class);
    	else i = new Intent(this, NavadnaBudilkaActivity.class); //drugače navadna (0)
    	
    	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	this.startActivity(i);
    	
    }
    
    
    

    /**
     * This is the object that receives interactions from clients.  See RemoteService
     * for a more complete example.
     */
    private final IBinder mBinder = new Binder() {
        @Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
		        int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };
}

