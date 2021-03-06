package elec291group2.com.project2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Login extends AppCompatActivity
{
    SharedPreferences sharedPreferences;
    EditText pinField;
    String pin;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        loginBtn = (Button) findViewById(R.id.login_button);
        pinField = (EditText) findViewById(R.id.pin_field);

        // pull stored PIN from sharedPrefs
        pin = sharedPreferences.getString("PIN", "Not set");

        // if the user has not set a PIN, prompt user to set a pin
        if (pin.equals("Not set"))
        {
            // create a alert dialog
            AlertDialog.Builder prompt = new AlertDialog.Builder(this);
            prompt.setMessage("Please set a security PIN.");
            final EditText input = new EditText(this);
            prompt.setView(input);
            input.setLayoutParams(new LinearLayout.LayoutParams(50, 30));
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setTransformationMethod(PasswordTransformationMethod.getInstance());

            // pressing OK saves the PIN
            prompt.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    // save PIN into sharedPrefs
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("PIN", input.getText().toString());
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Pin set.", Toast.LENGTH_SHORT).show();
                    pin = sharedPreferences.getString("PIN", "Not set");
                }
            });
            // pressing CANCEL exits out of the app
            prompt.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    Intent exitApp = new Intent(Intent.ACTION_MAIN);
                    exitApp.addCategory(Intent.CATEGORY_HOME);
                    exitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(exitApp);
                }
            });
            // display dialog on screen
            AlertDialog dialog = prompt.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener()
            {
                @Override
                public void onShow(DialogInterface dialog)
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                }
            });
            dialog.show();
        }

        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                // check if PIN matches the one saved in sharedPrefs
                if (pinField.getText().toString().equals(pin))
                {
                    // enter main menu if matches
                    Intent main = new Intent(getApplicationContext(), MainMenu.class);
                    startActivity(main);
                }
                // if PIN is wrong, clear field and tell user to try again
                else
                {
                    pinField.setText("");
                    Toast.makeText(getApplicationContext(), "Wrong PIN, try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
