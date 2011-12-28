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


import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.Toast;


public class OdstevalnikService_Service extends Service {
    NotificationManager mNM;
    
    @Override
    public void onCreate() 
    {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Thread thr = new Thread(null, mTask, "OdstevalnikService_Service");
        thr.start();
        
    }

    @Override
    public void onDestroy() 
    {
        
        mNM.cancel(5432222);

        Toast.makeText(this, "odstevalnik_service_finished", Toast.LENGTH_SHORT).show();
    }

    /**
     * The function that runs in our worker thread
     */
    Runnable mTask = new Runnable() {  
        public void run() 
        {
        	startPlaying();

            OdstevalnikService_Service.this.stopSelf();
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
    	Intent i = new Intent(this, IzklopOdstevalnikaActivity.class);
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

