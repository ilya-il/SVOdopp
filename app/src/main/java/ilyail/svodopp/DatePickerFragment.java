package ilyail.svodopp;

import android.os.Bundle;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.support.v4.app.DialogFragment;
import android.support.annotation.NonNull;

/*
    Caution: If your app supports versions of Android lower than 3.0,
    be sure that you call getSupportFragmentManager() to acquire an instance of FragmentManager.
    Also make sure that your activity that displays the time picker extends
    FragmentActivity instead of the standard Activity class.
*/

public class DatePickerFragment extends DialogFragment {
   private OnDateSetListener ondateSet;

    private int Year, Month, Day;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), ondateSet, Year, Month, Day);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        Year = args.getInt("year");
        Month = args.getInt("month");
        Day = args.getInt("day");
    }

    public void setCallBack(OnDateSetListener ondate) {
        ondateSet = ondate;
    }

}