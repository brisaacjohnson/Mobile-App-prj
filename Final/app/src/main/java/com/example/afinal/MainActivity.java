// MainActivity.java
// Calculates a bill total based on a tip percentage
package com.example.afinal;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity; // base class
import android.os.Bundle; // for saving state information
import android.text.Editable; // for EditText event handling
import android.text.TextWatcher; // EditText listener
import android.view.View;
import android.widget.EditText; // for bill amount input
import android.widget.SeekBar; // for changing the tip percentage
import android.widget.SeekBar.OnSeekBarChangeListener; // SeekBar listener
import android.widget.TextView; // for displaying text

import java.text.DateFormatSymbols;
import java.text.NumberFormat; // for currency formatting
import java.util.ArrayList;
import java.util.List;

import static android.content.DialogInterface.*;

// MainActivity class for the TIp Calculator app
public class MainActivity extends AppCompatActivity {

    // currency and percent formatter objects
    private static final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat =
            NumberFormat.getPercentInstance();
    private static final NumberFormat integerFormat =
            NumberFormat.getIntegerInstance();

    private static double amount = 0.0; // amount entered by user
    private static int years = 0; // years entered by user
    private static double percent = 0.0; // initial loan percentage
    private static double total;
    private static double payment;
    private static double t;
    public  static int len;
    private static TextView amountTextView; // shows formatted amount
    private static TextView yearTextView; // total loan months
    private static TextView percentTextView; // shows loan percentage
    private static TextView interestTextView; // shows calculated interest amount
    private static TextView totalTextView; // shows calculated total bill amount
    private static TextView paymentTextView; // shows calculated monthly payment
    // called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // call superclass's version
        setContentView(R.layout.activity_main); // inflate the GUI
        // get references to programmatically manipulated TextViews
        amountTextView = (TextView) findViewById(R.id.amountTextView);
        yearTextView = (TextView) findViewById(R.id.yearTextView);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        interestTextView = (TextView) findViewById(R.id.interestTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        paymentTextView = (TextView) findViewById(R.id.paymentTextView);
        interestTextView.setText(currencyFormat.format(0));
        totalTextView.setText(currencyFormat.format(0));
        paymentTextView.setText(currencyFormat.format(0));

        // set amountEditText's TextWatcher
        EditText amountEditText =
                (EditText) findViewById(R.id.amountEditText);
        amountEditText.addTextChangedListener(amountEditTextWatcher);

        // set monthEditText's TextWatcher
        EditText yearEditText =
                (EditText) findViewById(R.id.yearEditText);
        yearEditText.addTextChangedListener(yearEditTextWatcher);

        // set percentSeekBar's OnSeekBarChangeListener
        SeekBar percentSeekBar =
                (SeekBar) findViewById(R.id.percentSeekBar);
        percentSeekBar.setOnSeekBarChangeListener(seekBarListener);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment paym = new monthlyPayments();
                paym.setCancelable(true);
                paym.show(getSupportFragmentManager(), "Loan Results");
                //setContentView(R.layout.results);

            }
        });
    }

    //calculate and display tip and total amounts
    public void calculate() {
//static CharSequence[]
        percentTextView.setText(percentFormat.format(percent));
        double p = amount;
        int n = 12; // number of compound periods per year
        double r = percent; // rate
        t = years; // years
        //months = (int) (t*12);
         total = p * (Math.pow(1 + (r / n), (n * t)));
        double interest = total - amount;
        payment = total / (t * 12);
        interestTextView.setText(currencyFormat.format(interest));
        totalTextView.setText(currencyFormat.format(total));

        if (amount != 0 && years != 0) {
            paymentTextView.setText(currencyFormat.format(payment));
        }

       // return pay;
    }

    //listener object for the SeekBar's progress changed events
    private final OnSeekBarChangeListener seekBarListener =
            new OnSeekBarChangeListener() {
                // update percent, then call calculate
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    percent = progress / 100.0; // set percent based on progress
                    calculate(); // calculate and display tip and total
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            };

    // listener object for the EditText's text-changed events
    private final TextWatcher amountEditTextWatcher = new TextWatcher() {
        // called when the user modifies the bill amount
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            try { // get bill amount and display currency formatted value
                amount = Double.parseDouble(s.toString()) / 100.0;
                amountTextView.setText(currencyFormat.format(amount));
            } catch (NumberFormatException e) { // if s is empty or non-numeric
                amountTextView.setText("");
                amount = 0.0;
            }

            calculate(); // update the trip and total TextViews
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) {
        }
    };

   public static CharSequence [] remainng(){
        len = (int) t*12;
        CharSequence [] pay = new CharSequence[len];
        Double [] load = new Double[len];
        for (int p=0; p<len; p++) {
            load[p] = total;
        }
        for (int x = 0; x < len; x++) {
            load[x] = load[x]-payment*x; //or just x+1
            pay[x] = "Month " + (x+1) + ": " + currencyFormat.format(load[x]) + "\n  Monthly Payment: " + currencyFormat.format(payment);

            }
        return pay;
    }

    private final TextWatcher yearEditTextWatcher = new TextWatcher() {
        // called when the user modifies the months
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            try { // get years and display value
                years = Integer.parseInt(s.toString());
                yearTextView.setText(integerFormat.format(years));
            } catch (NumberFormatException e) { // if s is empty or non-numeric
                yearTextView.setText("");
                years = 0;
            }

            calculate(); // update the trip and total TextViews
            remainng();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) {
        }
    };
    public static class monthlyPayments extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle bundle) {
            CharSequence[] p = remainng();

            AlertDialog.Builder loanbreakdown = new AlertDialog.Builder(getActivity());
            loanbreakdown.setTitle("Loan Breakdown");
            loanbreakdown.setItems(p, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       //not needed
                    }
                });

            return loanbreakdown.create();

        }


    }

}