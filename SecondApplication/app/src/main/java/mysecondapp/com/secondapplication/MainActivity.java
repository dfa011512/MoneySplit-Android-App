package mysecondapp.com.secondapplication;

import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends ActionBarActivity {

    private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";      // used to store the instance state of BILL WITHOUT TIP
    private static final String CURRENT_TIP = "CURRENT_TIP";                // used to store the instance state of CURRENT TIP
    private static final String TOTAL_BILL = "TOTAL_BILL";                  // used to store the instance state of TOTAL BILL AFTER TIP
    private static final String PEOPLE = "COUNT_PEOPLE";                    // used to store the instance state of COUNT PEOPLE
    private static final String PER_PERSON = "PER_PERSON";                  // used to store the instance state of PER PERSON BILL

    private double billBeforeTip;           // stores the value retrieve from the bill before tip edit text
    private double tipAmount;               // stores the value retrieve from the tip amount spinner
    private double finalBill;               // stores the value retrieve from the final bill edit text
    private int countPeople;                // stores the value retrieve from the count people spinner
    private double perPersonBill;           // stores the value retrieve from the per person bill edit text

    private int pos_count_people;           // stores the position of the count people dropdown menu selected
    private int position_tip;               // stores the position of the tip amount dropdown menu selected

    EditText billBeforeTipET;
    EditText finalBillET;
    EditText perPersonBillET;

    Spinner tipAmountET;
    Spinner countPeopleET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {        // this method is called once at the stat of the application
        super.onCreate(savedInstanceState);

        // check the configuration orientation then accordingly select the portrait or landscape xml file
        setContentView(R.layout.activity_main);         // selects the portrait xml file

        // Alternate method
        /*if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            setContentView(R.layout.activity_main);         // selects the portrait xml file

        }
        else{

            setContentView(R.layout.landscape);             // selects the landscape xml file

        }*/

        // check if the instance state is new or not. If new insatnce state then set the default values of all the variables used otherwise retrieve the values from the saved instance state
        if(savedInstanceState == null){

            billBeforeTip = 0.0;
            tipAmount = 0.15;
            finalBill = 0.0;
            countPeople = 1;
            perPersonBill = 0.0;

        }else{

            billBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
            position_tip = savedInstanceState.getInt(CURRENT_TIP);
            finalBill = savedInstanceState.getDouble(TOTAL_BILL);
            countPeople = savedInstanceState.getInt(PEOPLE);
            perPersonBill = savedInstanceState.getDouble(PER_PERSON);
        }

        billBeforeTipET = (EditText)findViewById(R.id.billEditText);
        tipAmountET = (Spinner)findViewById(R.id.tipSpinner);
        finalBillET = (EditText)findViewById(R.id.finalBillEditText);
        countPeopleET = (Spinner)findViewById(R.id.countPeopleSpinner);
        perPersonBillET = (EditText)findViewById(R.id.perPersonEditText);


        billBeforeTipET.addTextChangedListener(billBeforeTipListener);      // calls the listener if there is a change in text on the bill edit text
        //tipAmountET.setOnItemSelectedListener(tipAmountListener);
        //countPeopleET.setOnItemSelectedListener(countPeopleListener);

    }

    // This method keeps track of the changes made in the bill edit field and then updates the billBeforeTip variable
    private TextWatcher billBeforeTipListener = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            try{

                billBeforeTip = Double.parseDouble(s.toString());       // retrieves the new value and updates the billBeforeTip variable
            }
            catch(NumberFormatException e){

                billBeforeTip =0.0;

            }

            updateTipAndFinalBill();        // calls the method to update the final bill calculation and display the value to the user
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    // This method keeps track of the item selected in the drop down menu of tip granularity
    private AdapterView.OnItemSelectedListener tipAmountListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            tipAmount = Double.parseDouble(parent.getItemAtPosition(position).toString());      // retrieves the new value and store it in the tipAmount variable
            position_tip = position;        // keep track of the position of the item in the drop down menu

            //System.out.println("\n---------------TIP ON TEXT CHANGE/POSITION----------------"+ tipAmount + " " + position_tip);
            updateTipAndFinalBill();        // calls the method to update the tip calculation
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    // This method keeps track of the item selected in the drop down menu of count people
    private AdapterView.OnItemSelectedListener countPeopleListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            countPeople = Integer.parseInt(parent.getItemAtPosition(position).toString());          // retrieves the new value and store it in the countPeople variable
            pos_count_people = position;            // keep track of the position of the item in the drop down menu

            //System.out.println("\n---------------PEOPLE ON TEXT CHANGE/POSITION----------------"+ countPeople + " " + pos_count_people);

            updatePeopleAndPerPersonBill();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    // This method performs the calculation of the final bill with tip
    private void updateTipAndFinalBill(){

        tipAmountET.setOnItemSelectedListener(tipAmountListener);       // call the method to see if there's any change in the tip amount

        tipAmount = Double.parseDouble(tipAmountET.getItemAtPosition(position_tip).toString());

        //System.out.println("\n---------------TIP AMT/POSITION----------------"+ tipAmount + " " + position_tip);

        finalBill = billBeforeTip + (billBeforeTip * tipAmount);

        finalBillET.setText(String.format("%.2f",finalBill));       // set the final bill value to the finalBillET and round to decimal place

        updatePeopleAndPerPersonBill();         // calls the method to calculate per person bill
    }

    // This method performs the division of the final bill based on the total number of people selected
    private void updatePeopleAndPerPersonBill(){

        countPeopleET.setOnItemSelectedListener(countPeopleListener);       // checks if there is any change in the count_people

        countPeople = Integer.parseInt(countPeopleET.getItemAtPosition(pos_count_people).toString());

        //System.out.println("\n---------------COUNT PEOPLE/POSITION----------------"+ countPeople + " " + pos_count_people);

        perPersonBill = finalBill / countPeople;

        perPersonBillET.setText(String.format("%.2f", perPersonBill));      // set the per person bill value to the perPersonET and round to 2 decimal place

    }

    // This method saves the instance state of all the values in a bundle
    protected void onSaveInstanceState(Bundle outState){

        System.out.println("------------------SAVE INSTANCE STATE CALLED------------------------");

        outState.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
        outState.putInt(CURRENT_TIP, tipAmountET.getSelectedItemPosition());
        outState.putDouble(TOTAL_BILL, finalBill);
        outState.putInt(PEOPLE, countPeopleET.getSelectedItemPosition());
        outState.putDouble(PER_PERSON, perPersonBill);

    }

    // restore the saved instance value and set it in their corresponding field
    protected void onRestoreInstanceState(Bundle savedState){

        System.out.println("------------------RESTORE INSTANCE STATE CALLED------------------------");

        Double bill_without_tip = savedState.getDouble(BILL_WITHOUT_TIP);
        Double final_bill_with_tip = savedState.getDouble(TOTAL_BILL);
        Double per_person_bill = savedState.getDouble(PER_PERSON);

        billBeforeTipET.setText(Double.toString(bill_without_tip));
        tipAmountET.setSelection(savedState.getInt(CURRENT_TIP, 0));
        finalBillET.setText(Double.toString(final_bill_with_tip));
        countPeopleET.setSelection(savedState.getInt(PEOPLE, 0));
        perPersonBillET.setText(Double.toString(per_person_bill));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
