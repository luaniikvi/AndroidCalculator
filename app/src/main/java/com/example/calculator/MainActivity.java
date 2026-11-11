package com.example.calculator;

import static com.example.calculator.ExpressionCalculator.evaluateExpression_toString;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.calculator.databinding.ActivityMainBinding;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("textview", binding.textView.getText().toString());
        outState.putString("textview2", binding.textView2.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        binding.textView.setText(savedInstanceState.getString("textview"));
        binding.textView2.setText(savedInstanceState.getString("textview2"));
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
                return Symbols.getAllOpIgnorePercent().indexOf(lastChar) != -1;
            }
            return false;
        };

        setDisplay.accept("0");


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
            String txt2 = binding.textView2.getText().toString();

            if (displayText.equals(txt2)) return;

            try {
                String ans = evaluateExpression_toString(displayText);
                setDisplay.accept(ans);
                if (!ans.equals(displayText))
                    binding.textView2.setText(displayText);
                else binding.textView2.setText(txt2);
            } catch (Exception e) {
                binding.textView2.setText(e.getMessage());
            }

        });


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


        binding.plusBtn.setOnClickListener(v -> {
            if (isDuplicateOp.getAsBoolean()) {
                replaceLastCharOfDisplay.accept(Symbols.PLUS);
            } else {
                appendDisplay.accept(Symbols.PLUS);
            }
        });

        binding.minusBtn.setOnClickListener(v -> {
            if (binding.textView.getText().toString().equals("0")) {
                setDisplay.accept(Symbols.MINUS);
            } else if (isDuplicateOp.getAsBoolean()) {
                replaceLastCharOfDisplay.accept(Symbols.MINUS);
            } else {
                appendDisplay.accept(Symbols.MINUS);
            }
        });

        binding.mulBtn.setOnClickListener(v -> {
            if (isDuplicateOp.getAsBoolean()) {
                replaceLastCharOfDisplay.accept(Symbols.MULTIPLY);
            } else {
                appendDisplay.accept(Symbols.MULTIPLY);
            }
        });

        binding.divBtn.setOnClickListener(v -> {
            if (isDuplicateOp.getAsBoolean()) {
                replaceLastCharOfDisplay.accept(Symbols.DIVIDE);
            } else {
                appendDisplay.accept(Symbols.DIVIDE);
            }
        });

        binding.percentBtn.setOnClickListener(v -> appendDisplay.accept(Symbols.PERCENT));

        binding.commaBtn.setOnClickListener(v -> {
            String currentText = binding.textView.getText().toString();

            String regex = "[" + java.util.regex.Pattern.quote(Symbols.getAllOp()) + "]";
            String[] parts = currentText.split(regex);

            String currentNumber = parts.length > 0 ? parts[parts.length - 1] : "";

            if (!currentNumber.contains(Symbols.DOT)) {
                if (currentText.isEmpty() || isDuplicateOp.getAsBoolean()) {
                    appendDisplay.accept("0" + Symbols.DOT);
                } else {
                    appendDisplay.accept(Symbols.DOT);
                }
            }
        });
    }
}