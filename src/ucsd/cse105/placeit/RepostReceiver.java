package ucsd.cse105.placeit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RepostReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int notificationId = intent.getIntExtra("notificationId", 0);
        
        LocationPlaceIt placeIt = Database.getPlaceIt(notificationId);
        placeIt.setDueDate(10);
		
		Database.save(placeIt, context);

		// remove notification
        NotificationHelper helper = new NotificationHelper(context);
        helper.dismissNotificationByID(notificationId);
    }
}
