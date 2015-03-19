package ilyail.svodopp;

// import android.util.Log;

import android.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Collections;
import java.util.TimeZone;

public class MainActivity extends ActionBarActivity {
    private TextView dateView;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateView = (TextView) findViewById(R.id.tvDate);

        /*
         *  Устанавливаем текущую зону в GMT,
         *  чтобы настройки зоны в телефоне не влияли
         *  на подсчет разницы между днями (летнее/зимнее время)
         */
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

        // при старте отображаем текущий день
        Calendar cal;
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        showDate(toDate(year, month, day));
    }

    /* обработчик кнопки, используется в layout */
    public void onClick(View view) {
        DatePickerFragment date_picker = new DatePickerFragment();

        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);

        date_picker.setArguments(args);

        date_picker.setCallBack(myDateListener);
        date_picker.show(getSupportFragmentManager(), "datePicker");
    }

    /* диалог выбора даты через DialogFragment */
    private final DatePickerDialog.OnDateSetListener myDateListener
            = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker p_view, int p_year, int p_month, int p_day) {
            /*
             * Костыль
             * OnDateSetListener() вызывается один раз, если в диалоге выбора даты нажать Esc/Back
             * и вызывается два раза, если нажать Done.
             * Сделана проверка p_view.isShown() - теперь логика работает только один раз и только
             * когда нажата Done.
             *
             * https://code.google.com/p/android/issues/detail?id=34833
             * https://code.google.com/p/android/issues/detail?id=34860
             */
            if (p_view.isShown()) {
                year = p_year;
                month = p_month;
                day = p_day;

                showDate(toDate(year, month, day));
            }
        }
    };

    /* отображение выбранной даты на экране */
    private void showDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy, EEEE", new Locale("ru"));

        dateView.setText(df.format(date));

        calcTimeShift(date);
    }

    /* получение Date из тройки year-month-day */
    private Date toDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /*
        Основная функция вычисления графика.
    */
    private void calcTimeShift(Date selected_date) {
        int days_mod;

        // базовая дата
        Date base_date;
        // порядок смен на базовую дату
        String[] base_shift_order = new String[4];
        // порядок времени на базовую дату
        String[] base_time_order = new String[4];

        TextView tvDay, tvNight, tvAfterNight, tvHoliday;
        TextView tvShift1, tvShift2, tvShift3, tvShift4;

        tvDay = (TextView) findViewById(R.id.tvDay);
        tvNight = (TextView) findViewById(R.id.tvNight);
        tvAfterNight = (TextView) findViewById(R.id.tvAfterNight);
        tvHoliday = (TextView) findViewById(R.id.tvHoliday);

        tvShift1 = (TextView) findViewById(R.id.tvShift1);
        tvShift2 = (TextView) findViewById(R.id.tvShift2);
        tvShift3 = (TextView) findViewById(R.id.tvShift3);
        tvShift4 = (TextView) findViewById(R.id.tvShift4);

        // базовая дата, на которую известен график - 11.03.2015
        base_date = toDate(2015, Calendar.MARCH, 11);

        /*
            порядок смен на базовую дату
            день     - 4
            ночь     - 3
            с ночи   - 2
            выходной - 1
        */
        base_shift_order[0] = getResources().getString(R.string.smena4);
        base_shift_order[1] = getResources().getString(R.string.smena3);
        base_shift_order[2] = getResources().getString(R.string.smena2);
        base_shift_order[3] = getResources().getString(R.string.smena1);

        // порядок времени на базовую дату
        base_time_order[0] = getResources().getString(R.string.holiday);
        base_time_order[1] = getResources().getString(R.string.afternight);
        base_time_order[2] = getResources().getString(R.string.night);
        base_time_order[3] = getResources().getString(R.string.day);

        // разница в днях по модулю 4 - остаток
        days_mod = (int)((selected_date.getTime() - base_date.getTime())/1000/60/60/24) % 4;

        // циклически сдвигаем базовый порядок и получаем порядок на выбранный день
        // учитывается знак days_mod
        Collections.rotate(Arrays.asList(base_shift_order), days_mod);
        Collections.rotate(Arrays.asList(base_time_order), days_mod);

        tvDay.setText(base_shift_order[0]);
        tvNight.setText(base_shift_order[1]);
        tvAfterNight.setText(base_shift_order[2]);
        tvHoliday.setText(base_shift_order[3]);

        tvShift1.setText(base_time_order[0]);
        tvShift2.setText(base_time_order[1]);
        tvShift3.setText(base_time_order[2]);
        tvShift4.setText(base_time_order[3]);
    }

    /* меню программы */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_about:
                showAboutDialog();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

    private void showAboutDialog(){
        DialogFragment about_dlg = new AboutDialog();
        about_dlg.show(getFragmentManager(), "AboutDialog");
    }
}
