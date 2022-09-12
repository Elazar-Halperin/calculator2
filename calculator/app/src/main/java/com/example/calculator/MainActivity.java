package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    public static final String FIRST_STRING_KEY = "first string key";
    public static final String OPERATOR_KEY = "operator key";
    public static final String EQUATION_KEY = "equation key";
    public static final String CURRENT_EDIT_TEXT_VALUE = "current editText value";


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

        tv_displayEquation.setSelected(true);

        equation = "";
        operation = "";
        first = "";

        initializeFABsAndTVs();

        if(savedInstanceState != null) {
            equation = savedInstanceState.getString(EQUATION_KEY);
            first = savedInstanceState.getString(FIRST_STRING_KEY);
            operation = savedInstanceState.getString(OPERATOR_KEY);

            et_showEquations.setText(savedInstanceState.getString(CURRENT_EDIT_TEXT_VALUE));

            tv_displayEquation.setText(equation);
        }

    }


    /**
     * Function to initialize all TextViews and the FloatingActionButtons, into a matrix
     * which will hold the views. Also we will set onClick and onTouch listener to the FloatingActionButtons.
     */
    // To avoid annoying warnings.
    @SuppressLint("ClickableViewAccessibility")
    private void initializeFABsAndTVs() {
        for (int i = 0; i < tl_calcHolder.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tl_calcHolder.getChildAt(i);
            for (int j = 0; j < tableRow.getChildCount(); j++) {
                FrameLayout constraintLayout = (FrameLayout) tableRow.getChildAt(j);
                FloatingActionButton fab_button = (FloatingActionButton) constraintLayout.getChildAt(0);
                TextView tv_operation = (TextView) constraintLayout.getChildAt(1);
                fab_buttons[i][j] = fab_button;

                fab_button.setOnClickListener(v -> {
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


                tv_buttonsText[i][j] = tv_operation;
            }
        }
    }

    /**
     * @param tv_operation gets an TextView which holds the string with the current operation
     * The operation might be a number or an operator!
     * Each operation gets his own function and his action.
     * Clear operator - resets all the values and the EditText and TextView text.
     *
     * Add, subtract, multiply and divide operators - sets the 'first' to the textView value and then it will clean the EditText and add it to the 'equation'
     * which will be displayed at the TextView.
     *
     * Equal operator - will make the final result, and display it!
     *
     * All is left is the number, and it will just add it to the 'first' and update it on the EditText.
     */
    private void getOperation(TextView tv_operation) {
        String operation = tv_operation.getText().toString().trim();

        // check which operation we do
        // if it is a number then we want to add it into our editText
        // if it's an operation then we want to erase the edit text and save into string.
        // if its clear operation then we want to cleat the edit text and the string.


        if (operation.equals(PLUS)) {
            addSymbolFunc(operation);
            return;
        }

        if (operation.equals(SUBTRACT)) {
            addSymbolFunc(operation);
            return;
        }

        if (operation.equals(MULTIPLY)) {
            addSymbolFunc(operation);
            return;
        }

        if (operation.equals(DIVIDE)) {
            addSymbolFunc(operation);
            return;
        }

        if (operation.equals(EQUALS)) {
            equalsFunction();
            return;
        }

        if (operation.equalsIgnoreCase(CLEAR)) {
            clearFunction();
            return;
        }

        // if we will get here then it means it's a number.

        String currentText = et_showEquations.getText().toString().trim();

        if (currentText.length() >= 15) {
            makeMessage("you reached the maximum length!");
            return;
        }

        et_showEquations.setText(new StringBuilder().append(currentText).append(operation).toString());

    }


    /**
     * Resets all the values and the displaying.
     */
    private void clearFunction() {
        first = "";
        operation = "";
        equation = "";

        et_showEquations.setText("");
        tv_displayEquation.setText("");

    }

    private void equalsFunction() {
        String second = et_showEquations.getText().toString().trim();

        if (operation.isEmpty() || first.isEmpty()) {
            return;
        }

        try {
            float firstFloat = Float.parseFloat(first);
            float secondFloat = Float.parseFloat(et_showEquations.getText().toString().trim());
            float result = 0f;
            if (operation.equals(PLUS)) {
                result = firstFloat + secondFloat;
            }
            if (operation.equals(SUBTRACT)) {
                result = firstFloat - secondFloat;
            }
            if (operation.equals(MULTIPLY)) {
                result = firstFloat * secondFloat;
            }
            if (operation.equals(DIVIDE)) {
                if (secondFloat == 0) {
                    makeMessage("Can not divide by 0!");
                    return;
                }
                result = firstFloat / secondFloat;
            }
            // the second will be always int.
            equation += second + " = ";

            Log.d("hellothere", result + "");

            String finalResult;
            String[] s = String.valueOf(result).split("\\.");
            // convert float into int.
            if (s[1].equals("0")) {
                finalResult = s[0];
            } else {
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

    /**
     * @param operator - string param which contains the chosen operator by the client.
     * the the function will all the 2 others functions.
     * the setOperationAndDisplayIt(operator) and the setFirstToEtContent() function.
     * Also we will check if the global operation is empty, if it is then we want to continue normally,
     * else we do not want it to make any changes.
     */
    private void addSymbolFunc(String operator) {
        if (!operation.isEmpty()) return;
        setOperationAndDisplayIt(operator);
        setFirstToEtContent();
    }


    /**
     * @param operator
     * Function gets string operator and update it to the global operation value.
     */
    private void setOperationAndDisplayIt(String operator) {
        operation = operator;
    }


    /**
     * Function to display and update the first number value of
     * the calculator and to display it in the tv_displayEquation.
     */
    private void setFirstToEtContent() {
        first = et_showEquations.getText().toString().trim();

        // In case the client have clicked the operator before entering a number.
        // in that case we will simply put 0 value to first number.
        if(first.isEmpty()) first = "0";

        et_showEquations.setText("");
        equation = first + " " + operation + " ";

        tv_displayEquation.setText(equation);
    }

    private void makeMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(FIRST_STRING_KEY, first);
        outState.putString(OPERATOR_KEY, operation);
        outState.putString(EQUATION_KEY, equation);
        outState.putString(CURRENT_EDIT_TEXT_VALUE, et_showEquations.getText().toString().trim());
        super.onSaveInstanceState(outState);
    }
}