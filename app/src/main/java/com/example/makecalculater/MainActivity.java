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

import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    int pos = -1;
    double firstNum = 0, secondNum = 0;
    TextView tv;
    TextView resultTv;
    String operater = "";
    Boolean isFirst = true;
    Deque<Integer> numStack = new LinkedList<>();
    Deque<Character> operatorStack = new LinkedList<>();

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
        operatorStack.push('(');
    }

    private void handleRight(String s) {
        operatorStack.push(')');
    }

    public void handleNumber(String s) {
        if(!resultTv.getText().toString().equals("")) {
            resultTv.setText("0");
        }
        if(isFirst && s.equals("0") && "".equals(tv.getText().toString())) {
            return;
        }
        if(isFirst && "0".equals(tv.getText().toString())) {
            tv.setText("");
        }
            tv.append(s);
    }
    public void handleDel() {
        if(isFirst && !tv.getText().toString().equals("")) {
            tv.append(".");
        }
    }
    public void handleOperator(String s) {
        String tvString = tv.getText().toString();
        if(isFirst && ("0".equals(tvString) || "".equals(tvString))) {
            tv.setText("");
        }
        if(tvString.length() > 0) {
            if(Math.abs(firstNum - Math.round(firstNum)) < 1e-9) {
                tv.setText(String.valueOf((int)firstNum));
            } else {
                tv.setText(String.valueOf(firstNum));
            }
            resultTv.setText("");
        }
        if(tvString.matches("[+\\-×÷]$")) {//排除前面有运算符还继续输入运算符的情况
            if(s.equals("-")) {
                if(tvString.charAt(tvString.length() - 1) != '-') {
                    tv.append(s);
                } else {
                    return;
                }
            } else {
                return;
            }
        }

    }
    public void calculateResult() {
        if(isFirst) {
            return;
        }
        String s = tv.getText().toString().substring(1);
        String regex = "[+\\-×÷%][-+]?\\d*\\.?\\d+(?:[eE][-+]?\\d+)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        matcher.find();
        String sub = matcher.group();
        secondNum = parseDouble(sub.substring(1));
        switch (operater) {
            case "+" :
                firstNum = firstNum + secondNum;
                break;
            case "-" :
                firstNum = firstNum - secondNum;
                break;
            case "×" :
                firstNum = firstNum * secondNum;
                break;
            case "÷" :
                firstNum = firstNum / secondNum;
                break;
            default:
                firstNum = firstNum % secondNum;
        }
        if(Math.abs(firstNum - Math.round(firstNum)) < 1e-9) {
            firstNum = (int)firstNum;
            resultTv.setText(String.valueOf((int)firstNum));
        } else {
            resultTv.setText(String.valueOf(firstNum));
        }
        isFirst = true;
        secondNum = 0;
    }

    public void backSpace() {
        String s = tv.getText().toString();
        if("".equals(s) || s.length() == 1) {
            tv.setText("0");
            firstNum = 0;
            return;
        }

        String sub = s.substring(1,s.length());
        if(sub.contains("+") || sub.contains("-") || sub.contains("×") || sub.contains("÷')")) {
            String[] twoNum = sub.split("[+\\-×÷]");
            if(twoNum.length == 1) {
                tv.setText(s.substring(0,s.length() - 1));
                isFirst = true;
            } else {
                tv.setText(s.substring(0,s.length() - 1));
                isFirst = false;
            }
            if(s.charAt(0) == '-') {
                twoNum[0] = "-" + twoNum[0];
                firstNum = parseDouble(twoNum[0]);
            } else {
                firstNum = parseDouble(s.charAt(0) + twoNum[0]);
            }

        } else {
            tv.setText(s.substring(0,s.length() - 1));
            firstNum = parseDouble(s.substring(0, s.length() - 1));
        }
        resultTv.setText("");

    }
    public void clearAll() {
        tv.setText("0");//这里一定要加“”，不然编译器以为这是id，即R.string.0
        resultTv.setText("");
        firstNum = 0;
        secondNum = 0;
    }
}