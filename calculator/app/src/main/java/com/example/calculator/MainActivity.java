package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    public static final String PLUS = "+";
    public static final String SUBTRACT = "-";
    public static final String MULTIPLY = "*";
    public static final String DIVIDE = "/";
    public static final String EQUALS = "=";
    public static final String CLEAR = "c";

    TableLayout tl_calcHolder;
    FloatingActionButton[][] fab_buttons;
    TextView[][] tv_buttonsText;
    TextView tv_displayEquation;
    EditText et_showEquations;

    String equation;
    String first;
    String operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab_buttons = new FloatingActionButton[4][4];
        tv_buttonsText = new TextView[4][4];
        tv_displayEquation = findViewById(R.id.tv_displayEquation);
        tl_calcHolder = findViewById(R.id.tl_calcHolder);
        et_showEquations = findViewById(R.id.et_showEquations);

        equation = "";
        operation = "";
        first = "";

        initializeFABsAndTVs();

    }


    // To avoid annoying warnings.
    @SuppressLint("ClickableViewAccessibility")

    private void initializeFABsAndTVs() {
        for(int i = 0; i < tl_calcHolder.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tl_calcHolder.getChildAt(i);
            for(int j = 0; j < tableRow.getChildCount(); j++) {
                FrameLayout constraintLayout = (FrameLayout) tableRow.getChildAt(j);
                FloatingActionButton fab_button = (FloatingActionButton) constraintLayout.getChildAt(0);
                TextView tv_operation = (TextView) constraintLayout.getChildAt(1);
                fab_buttons[i][j] = fab_button;

                fab_button.setOnClickListener( v -> {
                    getOperation(tv_operation);
                });


                fab_button.setOnTouchListener((v, event) -> {

                    switch (event.getAction()) {
                        // when the client touches the button.
                        case MotionEvent.ACTION_DOWN:
                            // minimize the TextView.
                            tv_operation.animate()
                                    .scaleY(0.7f)
                                    .scaleX(0.7f)
                                    .setDuration(30);
                            break;
                        // when the client releases the button
                        case MotionEvent.ACTION_UP:
                            // set it back to default
                            tv_operation.animate()
                                    .setStartDelay(30)
                                    .scaleY(1f)
                                    .scaleX(1f)
                                    .setDuration(60);
                            break;
                    }
                    // To make the overlapping button effect return false.
                    // if you don't want the effect return true;
                    return false;
                });


                tv_buttonsText [i][j] = tv_operation;
            }
        }
    }

    private void getOperation(TextView tv_operation) {
        String operation = tv_operation.getText().toString().trim();

        // check which operation we do
        // if it is a number then we want to add it into our editText
        // if it's an operation then we want to erase the edit text and save into string.
        // if its clear operation then we want to cleat the edit text and the string.

        if(operation.equals(PLUS)) {
            addSymbolFunc(operation);
            return;
        }

        if(operation.equals(SUBTRACT)) {
            addSymbolFunc(operation);
            return;
        }

        if(operation.equals(MULTIPLY)) {
            addSymbolFunc(operation);
            return;
        }

        if(operation.equals(DIVIDE)) {
            addSymbolFunc(operation);
            return;
        }

        if(operation.equals(EQUALS)) {
            equalsFunction();
            return;
        }

        if(operation.equalsIgnoreCase(CLEAR)) {
            clearFunction();
            return;
        }

        // if we will get here then it means it's a number.

        String currentText = et_showEquations.getText().toString().trim();

        if(currentText.length() >= 15) {
            makeMessage("you reached the maximum length!");
            return;
        }

        et_showEquations.setText(new StringBuilder().append(currentText).append(operation).toString());

    }

    private void clearFunction() {
        first = "";
        operation = "";
        equation = "";

        et_showEquations.setText("");
        tv_displayEquation.setText("");

    }

    private void equalsFunction() {
        if(operation.isEmpty() || first.isEmpty()) {
            return;
        }

        try {
            float firstFloat = Float.parseFloat(first);
            float secondFloat = Float.parseFloat(et_showEquations.getText().toString().trim());
            float result = 0f;
            if(operation.equals(PLUS)) {
                result = firstFloat + secondFloat;
            }
            if(operation.equals(SUBTRACT)) {
                result = firstFloat - secondFloat;
            }
            if(operation.equals(MULTIPLY)) {
                result = firstFloat * secondFloat;
            }
            if(operation.equals(DIVIDE)) {
                if (secondFloat == 0) {
                    makeMessage("Can not divide by 0!");
                    return;
                }
                result = firstFloat / secondFloat;
            }
            // the second will be always int.
            equation += (int) secondFloat + " = ";

            Log.d("hellothere", result +"");

            String finalResult;
            String [] s =  String.valueOf(result).split("\\.");
            // convert float into int.
            if(s[1].equals("0")) {
                finalResult = s[0];
            } else  {
                finalResult = String.valueOf(result);
            }

            et_showEquations.setText(finalResult);
            tv_displayEquation.setText(equation);

            operation = "";
            first = "";
            equation = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSymbolFunc(String symbol) {
        setOperationAndDisplayIt(symbol);
        setFirstToEtContent();
    }

    private void setOperationAndDisplayIt(String symbol) {
        operation = symbol;
    }

    private void setFirstToEtContent() {
        first = et_showEquations.getText().toString().trim();
        et_showEquations.setText("");
        equation = first + " " + operation + " ";
        tv_displayEquation.setText(equation);
    }

    private void makeMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}