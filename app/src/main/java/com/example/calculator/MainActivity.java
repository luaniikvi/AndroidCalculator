package com.example.calculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    Button btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,plusBtn,subBtn,mulBtn,divBtn,clearBtn,delBtn,percentBtn,equalBtn,commaBtn;
    TextView textView, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn0 = findViewById(R.id.btn_0);
        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btn5 = findViewById(R.id.btn_5);
        btn6 = findViewById(R.id.btn_6);
        btn7 = findViewById(R.id.btn_7);
        btn8 = findViewById(R.id.btn_8);
        btn9 = findViewById(R.id.btn_9);

        plusBtn = findViewById(R.id.plusBtn);
        subBtn  = findViewById(R.id.subBtn);
        mulBtn  = findViewById(R.id.mulBtn);
        divBtn  = findViewById(R.id.divBtn);
        delBtn  = findViewById(R.id.delBtn);
        percentBtn = findViewById(R.id.percentBtn);
        equalBtn   = findViewById(R.id.equalBtn);
        commaBtn   = findViewById(R.id.commaBtn);


        clearBtn  = findViewById(R.id.clearBtn);
        textView  = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        setDisplay("0");

        clearBtn.setOnClickListener(v -> {
            setDisplay("0");
        });
        delBtn.setOnClickListener(v -> {
            String str = textView.getText().toString();
            if (str.length() == 1) {
                setDisplay("0");
            }
            else setDisplay(str.substring(0, str.length()-1));
        });
        equalBtn.setOnClickListener(v -> {
            String displayText = textView.getText().toString();
            String ans = ExpressionCalculator.evaluateExpression(displayText).toString();
            setDisplay(ans);
            textView2.setText(displayText);
        });

        btn0.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (textView.getText().equals("0")) {
                    setDisplay("0");
                }
                else {
                    setDisplay(textView.getText() + "0");
                }

            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (textView.getText().equals("0")) {
                    setDisplay("1");
                }
                else {
                    setDisplay(textView.getText() + "1");
                }

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (textView.getText().equals("0")) {
                    setDisplay("2");
                }
                else {
                    setDisplay(textView.getText() + "2");
                }

            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (textView.getText().equals("0")) {
                    setDisplay("3");
                }
                else {
                    setDisplay(textView.getText() + "3");
                }

            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (textView.getText().equals("0")) {
                    setDisplay("4");
                }
                else {
                    setDisplay(textView.getText() + "4");
                }

            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (textView.getText().equals("0")) {
                    setDisplay("5");
                }
                else {
                    setDisplay(textView.getText() + "5");
                }

            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (textView.getText().equals("0")) {
                    setDisplay("6");
                }
                else {
                    setDisplay(textView.getText() + "6");
                }

            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (textView.getText().equals("0")) {
                    setDisplay("7");
                }
                else {
                    setDisplay(textView.getText() + "7");
                }

            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (textView.getText().equals("0")) {
                    setDisplay("8");
                }
                else {
                    setDisplay(textView.getText() + "8");
                }

            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (textView.getText().equals("0")) {
                    setDisplay("9");
                }
                else {
                    setDisplay((textView.getText() + "9"));
                }

            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (isDuplicateOp()){
                    replaceLastCharOfDisplay("+");
                }
                else appendDisplay("+");
            }
        });
        subBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (textView.getText().toString().equals("0"))
                    setDisplay("-");
                else if (isDuplicateOp()) replaceLastCharOfDisplay("-");
                else appendDisplay("-");
            }
        });
        mulBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (isDuplicateOp()){
                    replaceLastCharOfDisplay("×");
                }
                else appendDisplay("×");
            }
        });
        divBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (isDuplicateOp()){
                    replaceLastCharOfDisplay("÷");
                }
                else appendDisplay("÷");
            }
        });
        percentBtn.setOnClickListener(v -> {
            appendDisplay("%");
        });
        commaBtn.setOnClickListener(v -> {
            if(!textView.getText().toString().contains(".")) appendDisplay(".");
        });

        System.out.println("jelp");
        Log.i("Debug info", "helo");
    }


    @SuppressLint("SetTextI18n")
    private void setDisplay(String str) {
        if (str.length() > 30) return;
        textView.setText(str);
        textView2.setText("");
    }
    private void appendDisplay(String str){
        if (str == null) return;
        setDisplay(textView.getText().toString() + str);
    }
    private  void replaceLastCharOfDisplay (String c){
        String txtDisplay = textView.getText().toString();
        txtDisplay = txtDisplay.substring(0,txtDisplay.length()-1) + c;
        setDisplay(txtDisplay);
    }


    /**
     * Kiểm tra xem ký tự cuối cùng trên màn hình hiển thị có phải là một phép tính (+, -, x, /, %) hay không.
     *
     * @return {@code true} nếu ký tự cuối là phép tính (tránh nhập trùng lặp), ngược lại trả về {@code false}.
     */
    private boolean isDuplicateOp(){
        String txt = textView.getText().toString();
        if (!txt.isEmpty()) {
            char lastChar = txt.charAt(txt.length()-1);

            return ("+-x/%÷×".indexOf(lastChar) != -1);
        }
        return false;
    }
}