package com.example.calculator;

import static com.example.calculator.ExpressionCalculator.evaluateExpression_toString;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.calculator.databinding.ActivityMainBinding;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    Button btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,plusBtn,subBtn,mulBtn,divBtn,clearBtn,delBtn,percentBtn,equalBtn,commaBtn;
    TextView textView, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        WindowCompat.enableEdgeToEdge(getWindow());
        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindKeys();
    }

    @SuppressLint("SetTextI18n")
    private void bindKeys() {
        int maxPrecision = getResources().getInteger(R.integer.precision);

        Consumer<String> setDisplay = str -> {
            if (str.length() > maxPrecision) return;
            binding.textView.setText(str);
            binding.textView2.setText("");
        };

        Consumer<String> appendDisplay = str -> {
            if (str == null) return;
            setDisplay.accept(binding.textView.getText().toString() + str);
        };

        Consumer<String> replaceLastCharOfDisplay = c -> {
            String txtDisplay = binding.textView.getText().toString();
            if (!txtDisplay.isEmpty()) {
                txtDisplay = txtDisplay.substring(0, txtDisplay.length() - 1) + c;
                setDisplay.accept(txtDisplay);
            }
        };

        BooleanSupplier isDuplicateOp = () -> {
            String txt = binding.textView.getText().toString();
            if (!txt.isEmpty()) {
                char lastChar = txt.charAt(txt.length() - 1);
                return "+-x/%÷×".indexOf(lastChar) != -1;
            }
            return false;
        };

        setDisplay.accept("0");

        // 2. Các nút chức năng
        binding.clearBtn.setOnClickListener(v -> setDisplay.accept("0"));

        binding.delBtn.setOnClickListener(v -> {
            String str = binding.textView.getText().toString();
            if (str.length() <= 1) {
                setDisplay.accept("0");
            } else {
                setDisplay.accept(str.substring(0, str.length() - 1));
            }
        });

        binding.equalBtn.setOnClickListener(v -> {
            String displayText = binding.textView.getText().toString();
            try {
                String ans = evaluateExpression_toString(displayText,this);
                setDisplay.accept(ans);
            } catch (Exception e) {
                binding.textView2.setText(e.getMessage());
                return;
            }
            binding.textView2.setText(displayText);
        });

        // 3. Các nút số (0 - 9)
        Button[] numButtons = {
                binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
                binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9
        };

        for (Button btn : numButtons) {
            btn.setOnClickListener(v -> {
                String digit = btn.getText().toString();
                if (binding.textView.getText().toString().equals("0")) {
                    setDisplay.accept(digit);
                } else {
                    appendDisplay.accept(digit);
                }
            });
        }

        // 4. Các nút phép tính (giữ riêng từng nút và ép ký tự hiển thị như code cũ)
        binding.plusBtn.setOnClickListener(v -> {
            if (isDuplicateOp.getAsBoolean()) {
                replaceLastCharOfDisplay.accept("+");
            } else {
                appendDisplay.accept("+");
            }
        });

        binding.subBtn.setOnClickListener(v -> {
            if (binding.textView.getText().toString().equals("0")) {
                setDisplay.accept("-");
            } else if (isDuplicateOp.getAsBoolean()) {
                replaceLastCharOfDisplay.accept("-");
            } else {
                appendDisplay.accept("-");
            }
        });

        binding.mulBtn.setOnClickListener(v -> {
            if (isDuplicateOp.getAsBoolean()) {
                replaceLastCharOfDisplay.accept("×");
            } else {
                appendDisplay.accept("×");
            }
        });

        binding.divBtn.setOnClickListener(v -> {
            if (isDuplicateOp.getAsBoolean()) {
                replaceLastCharOfDisplay.accept("÷");
            } else {
                appendDisplay.accept("÷");
            }
        });

        binding.percentBtn.setOnClickListener(v -> appendDisplay.accept("%"));

        binding.commaBtn.setOnClickListener(v -> {
            if (!binding.textView.getText().toString().contains(".")) {
                appendDisplay.accept(".");
            }
        });
    }
}