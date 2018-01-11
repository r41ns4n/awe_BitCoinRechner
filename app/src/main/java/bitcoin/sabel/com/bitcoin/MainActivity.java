package bitcoin.sabel.com.bitcoin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends Activity {

    private Button btn_change, btn_exit, btn_new, btn_catch;
    private TextView tv_wilkommen, tv_kurs;
    private EditText et_euro, et_bitcoin;
    private double faktorBitcoinKursInEuro;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_change = findViewById(R.id.btn_change);
        btn_exit = findViewById(R.id.btn_exit);
        btn_new = findViewById(R.id.btn_new);
        btn_catch = findViewById(R.id.btn_catch);
        tv_wilkommen = findViewById(R.id.tv_willkommen);
        tv_kurs = findViewById(R.id.tv_kurs);
        et_euro = findViewById(R.id.et_euro);
        et_bitcoin = findViewById(R.id.et_bitcoin);
        btn_change.setEnabled(false);

        // Shared Preferences
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putFloat("bitcoinkurs", (float) faktorBitcoinKursInEuro);
        editor.commit();
        faktorBitcoinKursInEuro = sharedPreferences.getFloat("bitcoinkurs", 0);

        // Event Button Change
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_euro.getText().toString().length() > 0) {
                    String euro_text = et_euro.getText().toString().replace(',', '.');
                    try {
                        Double euro_text_double = Double.parseDouble(euro_text);
                        euro_text_double = euro_text_double / faktorBitcoinKursInEuro;
                        String bitcoin_text = new Double(euro_text_double).toString();
                        et_bitcoin.setText(bitcoin_text + " BTC");
                    } catch (NumberFormatException e) {
                        et_euro.setText("");
                    } // END TRY-CATCH BLOCK

                } else {
                    String bitcoin_text = et_bitcoin.getText().toString().replace(',', '.');
                    try {
                        Double bitcoin_text_double = Double.parseDouble(bitcoin_text);
                        bitcoin_text_double = bitcoin_text_double * faktorBitcoinKursInEuro;
                        String euro_text = new Double(bitcoin_text_double).toString();
                        et_euro.setText(euro_text + " EURO");
                    } catch (NumberFormatException e) {
                        et_bitcoin.setText("");
                    } // END TRY-CATCH BLOCK
                } // END ELSE-BLOCK
            } // END public void onClick(View view)
        });

        // Event Button Kurs holen
        btn_catch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDownloadThread myDownloadThread = new MyDownloadThread();
                myDownloadThread.execute();
                tv_kurs.setText("Aktueller Kurs: " + faktorBitcoinKursInEuro);
            }
        });

        // Event Button Exit
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Event Button New
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_bitcoin.setText("");
                et_euro.setText("");
            }
        });

        // Event Only when Text is insert
        et_euro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn_change.setEnabled(true);
            }
        });

        et_bitcoin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn_change.setEnabled(true);
            }
        });

    } // END protected void onCreate

    // Inner Class
    public class MyDownloadThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            URL url = null;
            URLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                url = new URL("https://bitaps.com/api/ticker/average");
                urlConnection = url.openConnection();
                inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String jsonZeile = null;
                String zeile = null;
                while ((zeile = bufferedReader.readLine()) != null) {
                    jsonZeile = zeile;
                } // END WHILE

                JSONObject jsonObject = new JSONObject(jsonZeile);
                JSONObject fx_rates = jsonObject.getJSONObject("fx_rates");
                faktorBitcoinKursInEuro = fx_rates.getDouble("eur");

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } // END TRY-CATCH-BLOCK
            return null;
        } // END protected Void doInBackground(Void... voids)
    }  // END INNER CLASS

} // END  class MainActivity extends Activity
