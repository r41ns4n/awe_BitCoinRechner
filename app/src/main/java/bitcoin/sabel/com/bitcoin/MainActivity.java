package bitcoin.sabel.com.bitcoin;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private Button btn_change, btn_exit;
    private TextView tv_wilkommen, tv_bitcoin;
    private EditText et_euro;
    private double bitcoinKurs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_change = findViewById(R.id.btn_change);
        btn_exit = findViewById(R.id.btn_exit);
        tv_wilkommen = findViewById(R.id.tv_willkommen);
        et_euro = findViewById(R.id.et_euro);
        tv_bitcoin = findViewById(R.id.tv_bitcoin);
        btn_change.setEnabled(false);
        bitcoinKurs = 2.5;


        // Event Button Change
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String euro_text = et_euro.getText().toString().replace(',', '.');
                    Double euro_text_double = Double.parseDouble(euro_text);
                    euro_text_double = euro_text_double * bitcoinKurs;
                    String bitcoin_text = new Double(euro_text_double).toString();
                    tv_bitcoin.setText(bitcoin_text);
            }
        });

        // Event Button Exit
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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


    } // END protected void onCreate
} // END  class MainActivity extends Activity
