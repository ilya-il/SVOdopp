package ilyail.svodopp;

import android.os.Bundle;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class AboutDialog extends DialogFragment implements OnClickListener {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.action_about)
                .setNegativeButton(R.string.btn_ok, this)
                .setMessage("График смен ДОПП ОАО МАШ" + "\n\n" +
                            "Версия " + BuildConfig.VERSION_NAME + "\n\n" +
                            "https://github.com/ilya-il"
                );
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}
