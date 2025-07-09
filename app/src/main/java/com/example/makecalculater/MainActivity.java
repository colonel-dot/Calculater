package com.example.makecalculater;

import static java.lang.Double.parseDouble;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    StringBuilder infixSb = new StringBuilder();
    TextView tv;
    TextView resultTv;
    String operater = "";
    Boolean isFirst = true;
    int leftNumber = 0, rightNumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        //沉浸式全屏模式的关键实现

        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        resultTv = findViewById(R.id.resultTv);
        Button bt0 = findViewById(R.id.bt0);
        bt0.setOnClickListener(this);
        Button bt1 = findViewById(R.id.bt1);
        bt1.setOnClickListener(this);
        Button bt2 = findViewById(R.id.bt2);
        bt2.setOnClickListener(this);
        Button bt3 = findViewById(R.id.bt3);
        bt3.setOnClickListener(this);
        Button bt4 = findViewById(R.id.bt4);
        bt4.setOnClickListener(this);
        Button bt5 = findViewById(R.id.bt5);
        bt5.setOnClickListener(this);
        Button bt6 = findViewById(R.id.bt6);
        bt6.setOnClickListener(this);
        Button bt7 = findViewById(R.id.bt7);
        bt7.setOnClickListener(this);
        Button bt8 = findViewById(R.id.bt8);
        bt8.setOnClickListener(this);
        Button bt9 = findViewById(R.id.bt9);
        bt9.setOnClickListener(this);
        Button btadd = findViewById(R.id.btadd);
        btadd.setOnClickListener(this);
        Button btbac = findViewById(R.id.btbac);
        btbac.setOnClickListener(this);
        Button btmin = findViewById(R.id.btmin);
        btmin.setOnClickListener(this);
        Button btmul = findViewById(R.id.btmul);
        btmul.setOnClickListener(this);
        Button btdiv = findViewById(R.id.btdiv);
        btdiv.setOnClickListener(this);
        Button btc = findViewById(R.id.btc);
        btc.setOnClickListener(this);
        Button btdel = findViewById(R.id.btdel);
        btdel.setOnClickListener(this);
        Button btequ = findViewById(R.id.btequ);
        Button btLeft = findViewById((R.id.btleft));
        btLeft.setOnClickListener(this);
        Button btRight = findViewById(R.id.btright);
        btRight.setOnClickListener(this);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor("#D2691E"));
        gradientDrawable.setCornerRadius(15);
        btequ.setBackground(gradientDrawable);
        btequ.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String ButtonText = ((Button)v).getText().toString();
        switch(ButtonText) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
                handleNumber(ButtonText);
                break;
            case ".":
                handleDel();
                break;
            case "+":
            case "-":
            case "×":
            case "÷":
            case "%":
                handleOperator(ButtonText);
                break;
            case "(":
                handleLeft(ButtonText);
                break;
            case ")":
                handleRight(ButtonText);
                break;
            case "=":
                calculateResult();
                break;
            case "C":
                clearAll();
                break;
            case "←" :
                backSpace();

        }

    }

    private void handleLeft(String s) {
        String tvString = tv.getText().toString();
        if(tvString.length() > 0 && !isOperator(tvString.charAt(tvString.length() - 1))) {
            infixSb.append("×");
            tv.append("×");
        }
        tv.append(s);
        infixSb.append(s);
        ++leftNumber;
    }

    private void handleRight(String s) {
        if(leftNumber < rightNumber + 1) {
            return;
        }
        tv.append(s);
        infixSb.append(s);
        ++rightNumber;
    }

    public void handleNumber(String s) {
        tv.append(s);
        infixSb.append(s);
    }


    public void handleDel() {
        if(isFirst && !tv.getText().toString().equals("")) {
            tv.append(".");
            infixSb.append(".");
        }
    }


    public void handleOperator(String s) {
        char ch = s.charAt(0);
        String tvString = tv.getText().toString();
        int len = tvString.length();
        if (ch == '-' && (len == 0 || isOperator(tvString.charAt(len - 1)) || tvString.charAt(len - 1) == '(')) {
            tv.append(s);
            infixSb.append(s);
            return;
        }
        if(ch != '-') {
            if(len == 0 || isOperator(tvString.charAt(len - 1))) {
                return;
            }
        }
        if(ch == '-' && len > 0 && tvString.charAt(len - 1) == '-') {
            return;
        }

        tv.append(s);
        infixSb.append(s);
    }
    public void calculateResult() {
        if(tv.getText().toString().isEmpty()) {
            return;
        }
        char lastChar = infixSb.charAt(infixSb.length() - 1);
        if(isOperator(infixSb.charAt(infixSb.length() - 1))) {
            while(infixSb.length() > 0 && isOperator(infixSb.charAt(infixSb.length() - 1))) {
                infixSb.deleteCharAt(infixSb.length() - 1);
            }
        }
        Deque<Character> infixStack = new LinkedList<>();
        String infix = infixSb.toString();
        StringBuilder postfixSb = new StringBuilder();
        int len = infix.length();
        for(int i = 0; i < len; ++i) {
            char ch = infix.charAt(i);
            if (Character.isDigit(ch) || ch == '.' || (ch == '-' && (i == 0 || (i > 0 &&(infix.charAt(i - 1) == '(') || isOperator(infix.charAt(i - 1)))))) {
                if (ch == '-') {
                    postfixSb.append(ch); // 保留负号
                    i++;
                }
                while (i < infix.length() && (Character.isDigit(infix.charAt(i)) || infix.charAt(i) == '.')) {
                    postfixSb.append(infix.charAt(i++));
                }
                postfixSb.append(" ");
                --i;
            }
            else if(ch == '(') {
                infixStack.push('(');
            }
            else if(ch == ')') {
                while(!infixStack.isEmpty() && infixStack.peek() != '(') {
                    char c = infixStack.pop();
                    postfixSb.append(c).append(" ");
                }
                infixStack.pop();

            }
            else if(isOperator(ch)){
                while (!infixStack.isEmpty() && getPriority(infixStack.peek()) >= getPriority(ch)) {
                    postfixSb.append(infixStack.pop()).append(" ");
                }
                infixStack.push(ch);
            }
        }
        while(!infixStack.isEmpty()) {
            postfixSb.append(infixStack.pop()).append(" ");
        }
        Deque<BigDecimal> calculateStack = new LinkedList<>();
        String[] tokens = postfixSb.toString().split("\\s+");
        for(String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?")) { // 数字
                calculateStack.push(new BigDecimal(token));
            } else if (isOperator(token.charAt(0))) {
                BigDecimal b = calculateStack.pop();
                BigDecimal a = calculateStack.pop();
                BigDecimal c = calculate(a, b, token.charAt(0));
                if(c == null) {
                    return;
                }
                calculateStack.push(c);
            }
        }
        BigDecimal temp = calculateStack.peek();
        if (temp.scale() <= 0 || temp.stripTrailingZeros().scale() <= 0) {
            // 是整数，显示为整数
            resultTv.setText(temp.setScale(0, RoundingMode.DOWN).toString());
        } else {
            // 是小数，保留3位
            resultTv.setText(temp.setScale(3, RoundingMode.HALF_UP).toString());
        }
    }

    private  BigDecimal calculate(BigDecimal a, BigDecimal b, char op) {
        if(op == '+') {
            return a.add(b);
        } else if (op == '-') {
            return a.subtract(b);
        } else if (op == '×') {
            return a.multiply(b);
        } else {
            if((b.compareTo(BigDecimal.ZERO) == 0)) {
                resultTv.setText("错误");
                return null;
            }
            return a.divide(b,3, RoundingMode.DOWN);
        }
    }

    public boolean isOperator(char ch) {
        if(ch == '×' || ch == '÷' || ch == '+' || ch == '-') {
            return true;
        } else {
            return false;
        }
    }

    public int getPriority(char ch) {
        if(ch == '+' || ch == '-') {
            return 1;
        }
        else if(ch == '×' || ch == '÷') {
            return 2;
        } else {
            return 0;
        }
    }

    public void backSpace() {
        String s = tv.getText().toString();
        if("".equals(s) || s.length() == 1) {
            tv.setText("");
            resultTv.setText("");
            infixSb = new StringBuilder();
        }
        else {
            tv.setText(s.substring(0, s.length() - 1));
            infixSb = new StringBuilder(tv.getText().toString());
            resultTv.setText("");
        }
        if(s.charAt(s.length() - 1) == ')') {
            --rightNumber;
        }
        if(s.charAt(s.length() - 1) == '(') {
            --leftNumber;
        }
    }
    public void clearAll() {
        tv.setText("");//这里一定要加“”，不然编译器以为这是id，即R.string.0
        resultTv.setText("");
        infixSb = new StringBuilder();
    }
}