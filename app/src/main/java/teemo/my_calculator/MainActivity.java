package teemo.my_calculator;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    /*계산기
    *이 계산기는 갤놋4에는 계산기가 없어서 그냥 심심풀이로 만들어 본 계산기이다
    *최대한 쉽게 만드려고 하였다
    *각 함수에 주석이 달려있으니 읽어보도록하자
    */
    //this is for fun!
    private String[] verse = {
            "항상 기뻐하라 쉬지말고 기도하라 범사에 감사하라\n이것이 그리스도 예수 안에서 너희를 향하신 하나님의 뜻이니라" +
                    "\n데살로니가전서 5장 16 - 18절",
            "내가 여호와를 항상 내 앞에 모심이여,\n그가 내 우편에 계시므로 내가 요동치 아니하리로다" +
                    "\n시편 16장 8절",
            "네 짐을 여호와께 맡겨 버리라.\n 너를 붙드시고 의인의 요동함을 영영히 허락지 아니하시리로다" +
                    "\n시편 55편 22절",
            "눈물을 흘리며 씨를 뿌리는 자는 기쁨으로 거두리로다.\n 울며 씨를 뿌리러 나가는 자는 정녕 기쁨으로 그단을 가지고 돌아오리로다" +
                    "\n시편 126장 5 - 6절",
            "주께서 심지가 견고한 자를 평강에 평강으로 지키시리니 이는 그가 주를 의뢰함이니이다" +
                    "\n이사야 26장 3절",
            "두려워 말라, 내가 너와 함께 함이니라.\n 놀라지말라 나는 네 하나님이 됨이니라.\n 내가 너를 굳세게 하리라. " +
                    "참으로 너를 도와주리라.\n 참으로 나의 의로운 오른손으로 너를 붙들리라" +
                    "\n이사야 41장 10절",
            "구하라 그러면 너희에게 주실것이요.\n 찾으라, 그러면 찾을것이요.\n 문을 두드리라, " +
                    "그러면 너희에게 열릴 것이니,\n 구하는 이마다 얻을 것이요 찾는 이가 찾을 것이요" +
                    "두드리는 이에게 열릴 것이니라"+
                    "\n마태복음 7장 7 - 8절"
    };
    private String[] response = {
            "하나님은 당신을 정말 사랑하십니다",
            "사랑합니다!",
            "할렐루야!"
    };
    //for calculating number
    //and to record the equation
    //we determine what to do based on information in the array
    // [0] - place of target [1] - place of operation [2] - place of targetee
    private String[] record = new String[3];
    private String res="";
    //connecting gui to functionality
    private TextView input, result;
    private ImageView teemo;
    Boolean equal = false;

    Random r = new Random();
    Vibrator vi;
    long[] pattern ={0,10,100,1000};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init initial values
        input =(TextView) findViewById(R.id.input);
        result=(TextView) findViewById(R.id.result);
        teemo = (ImageView) findViewById(R.id.teemo);
        clear(true);
        vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //below is for fun and practice only!
        teemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                a.setCancelable(false);
                a.setTitle("틈새 말씀타임!");
                a.setMessage("\n"+verse[r.nextInt(verse.length)])
                        .setCancelable(false)
                        .setPositiveButton("아멘!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),response[r.nextInt(response.length)], Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                vi.cancel();
                            }
                        });
                AlertDialog b = a.create();
                vi.vibrate(pattern,0);
                b.show();
            }
        });
    }
    /*clear method has two function!
     if the parameter is set true,
     it will clear the output!
     or if it is set to false, it will clear out the data
     */
    public void clear(Boolean b) {
        for (int i = 0; i < record.length; i++) {
            record[i] = i % 2 == 0 ? "0" : null;
        }
        equal=false;
        clearOut(b);
    }public void clearOut(Boolean b){
        if(b){
            input.setText("0");
            result.setText("0");
        }
    }

    //this method is called when an user pressed a button.
    //it will return a local String of current contents in the array
    public String makeEqation(){
        String eqation="";
        //we don wanna print "null" in the screen
        for(String s: record) if(s!=null) if(!s.equals("0")) eqation = eqation + s;
        return eqation;
    }

    //check if operation has been clicked
    public Boolean checkOperation(){
        //move targetee to target
        /*if operation button is pressed first time,
         we just need to move the number to target*/
        if(record[1] == null) {
            /*we need to make sure that there is no data in record[0]
            * so that the result would not be wiped! when a number is pressed
             * it will just move the data! */
            if (record[0].equals("0")) {
                record[0] = record[2];
                record[2] = "0";
            /*data in record[0] and not in record[1] means that equal button has been pressed
            * there are two cases
            *   -> the user wants to re-start the calculator
            *   -> the user wants to use the result as an input!
            */
            }else {
                res = record[0];
                clear(true);
                record[0] = res;
            } return false;
        }return true;
    }

    //it will determine what app is gonna do based on saved operaion in record[1]
    public void operation(){
        if(record[1].equals(" + ")){
            res = String.valueOf(Float.parseFloat(record[0]) + Float.parseFloat(record[2]));
        }else if(record[1].equals(" - ")){
            res = String.valueOf(Float.parseFloat(record[0]) - Float.parseFloat(record[2]));
        }else if(record[1].equals(" * ")){
            res = String.valueOf(Float.parseFloat(record[0]) * Float.parseFloat(record[2]));
        }else if(record[1].equals(" / ")){
            //make sure that we don allow to divide a number with 0!
            if(record[2] =="0"){
                Toast.makeText(getApplicationContext(), "0으로 못나눠!!삐뿌야!", Toast.LENGTH_SHORT).show();
                clear(true);
            }else res = String.valueOf(Float.parseFloat(record[0]) / Float.parseFloat(record[2]));
        }else if(record[1] ==null){
            Toast.makeText(getApplicationContext(), "그 뭐시기, 연산기호 선택안하셧소", Toast.LENGTH_SHORT).show();
            clear(true);
        }
    }
    //update input box!
    public void updateNum(String s){
        //append pressed value to the string
        if(record[2]=="0") record[2] = s;
        else record[2] = record[2] + s;
        //update the textbox!
        input.setText(makeEqation());
    }

    //when an user pressed a operaion button
    public void updateEqa(String s) {
        //case there is already an operation
        if(checkOperation()){
            operation();
            record[0]=res;
            record[2] ="0";
        }
        //we don need to add case for first time.
        //all we need to do for first time is to add the operaion!
        record[1] = " "+s+" ";
        input.setText(makeEqation());
    }

    public void equalPressed(View v){
        if(!equal && checkOperation()) {
            //calculate based on the inputs
            operation();
            //will output the result and the equation
            input.setText(makeEqation());
            result.setText(res);
            //then we need to wipe out only data!
            clear(false);
            //it will move result as the first argument of the equation
            record[0] = res;
            //the set the equal boolean
            equal=true;
        }else{
            //it will clear out
            clear(true);
            equal=false;
        }
    }

    public void clearPressed(View v){
        clear(true);
    }
    public void onePressed(View v){
        updateNum("1");
    }
    public void twoPressed(View v){
        updateNum("2");
    }
    public void threePressed(View v){
        updateNum("3");
    }
    public void fourPressed(View v){
        updateNum("4");
    }
    public void fivePressed(View v){
        updateNum("5");
    }
    public void sixPressed(View v){
        updateNum("6");
    }
    public void sevenPressed(View v){
        updateNum("7");
    }
    public void eightPressed(View v){
        updateNum("8");
    }public void ninePressed(View v){
        updateNum("9");
    }
    public void zeroPressed(View v){
        updateNum("0");
    }public void dotPressed(View v) {
        updateNum(".");
    }
    public void plusPressed(View v) {
        updateEqa("+");
    }
    public void subPressed(View v){
        updateEqa("-");
    }
    public void mulPressed(View v){
        updateEqa("*");
    }
    public void divPressed(View v){
        updateEqa("/");
    }
}
