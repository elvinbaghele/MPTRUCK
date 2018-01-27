package com.ycce.mptruck;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class OTP extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    //Various states for updating the UI
    private static final int STATE_INITIALIZED = 1; //show only the phone number field and start button
    private static final int STATE_CODE_SENT = 2;//enable the verification field
    private static final int STATE_VERIFY_FAILED = 3; // Verification has failed, enable all fields
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference ,databaseReference1;
    private FirebaseDatabase firebaseDatabase;

    // [END declare_auth]

    //Progress Dialog
    private ProgressDialog dialog;


    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    //Declaring Views and elements
    private ViewGroup mPhoneNumberViews;
    private EditText mPhoneNumberField;
    private EditText mVerificationField;
    private Button mStartButton;
    private Button mResendButton;

    private ViewGroup mSignedInViews;
    private ImageView imageView;
    private EditText mEmailField;
    private EditText mAddressField;
    private Button mSignOutButton;
    private Button mSave;

    private int PICK_IMAGE_REQUEST = 1;
    private Uri uri;

    //contains all the assignments of elements
    public void init(){
        //assigning Progress Dialog
        dialog = new ProgressDialog(this);
        // Assign views
        mPhoneNumberViews = (ViewGroup) findViewById(R.id.phone_auth_fields);
        mSignedInViews = (ViewGroup) findViewById(R.id.signed_in_buttons);

        //Assigning Fields
        mPhoneNumberField = (EditText) findViewById(R.id.field_phone_number);
        mVerificationField = (EditText) findViewById(R.id.field_verification_code);
        mEmailField = (EditText) findViewById(R.id.email);
        mAddressField = (EditText) findViewById(R.id.address);

        //Assigning Buttons
        mStartButton = (Button) findViewById(R.id.button_start_verification);
        mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mResendButton = (Button) findViewById(R.id.button_resend);
        mSave = (Button) findViewById(R.id.button_save);

        //Assigning profile image view
        imageView= (ImageView) findViewById(R.id.profile_image);
        imageView.setOnClickListener(this);

        // Assign click listeners
        mStartButton.setOnClickListener(this);
        mSave.setOnClickListener(this);
        mResendButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        //startActivity(new Intent(this,MainActivity.class)); //it was done at the time of development to skip the login

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        //Assigning
        init();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        //this callback method works asynchronously to verify the phone no.
        //This is called from a
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {

        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("On Start: "," 1");
        updateUI(currentUser);

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
        }
        // [END_EXCLUDE]
    }
    // [END on_start_check_user]

    //When you exit from app
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    //when you resume
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    //It internally calls mcallback to send the PhoneAuth Credential
    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    //Signout from App, by signout from firebase
    private void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    // main funtion of Update UI, that updates the UI of the App based on the uiState
    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                enableViews(mStartButton, mPhoneNumberField);
                disableViews( mResendButton, mVerificationField);
                //mDetailText.setText(null);
                break;
            case STATE_CODE_SENT:
                // Code sent state, enable the verification field, the
                enableViews( mResendButton, mPhoneNumberField, mVerificationField);
                disableViews(mStartButton);
               // mDetailText.setText(R.string.status_code_sent);
                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, enable all fields
                enableViews(mStartButton,  mResendButton, mPhoneNumberField,
                        mVerificationField);
                Toast.makeText(this,"Unable To verify",Toast.LENGTH_LONG).show();
                //mDetailText.setText(R.string.status_verification_failed);
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                disableViews(mStartButton, mResendButton, mPhoneNumberField,
                        mVerificationField);
                //mDetailText.setText(R.string.status_verification_succeeded);

                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode()); //set the verification code of vefication number field
                        signInWithPhoneAuthCredential(cred); //call the method with credentials to sign in
                    } else {
                        mVerificationField.setText(R.string.instant_validation);
                    }
                }
                //firebaseDatabase = FirebaseDatabase.getInstance();
                break;
            case STATE_SIGNIN_FAILED:

                Toast.makeText(this,"Sign In Failed",Toast.LENGTH_LONG).show();
                // No-op, handled by sign-in check
                //mDetailText.setText(R.string.status_sign_in_failed);
                break;
            case STATE_SIGNIN_SUCCESS:
                //Give the access to the firebase database
                firebaseDatabase = FirebaseDatabase.getInstance();

                // Np-op, handled by sign-in check
                break;
        }

        if (user == null) {
            // Signed out
            mPhoneNumberViews.setVisibility(View.VISIBLE);
            //mSignedInViews.setVisibility(View.GONE);

            //mStatusText.setText(R.string.signed_out);;
        } else {
            // Signed in
                disableViews(mSave);
              if(checkInDatabase(user)) {
                  mPhoneNumberViews.setVisibility(View.GONE);
                  mSignedInViews.setVisibility(View.VISIBLE);
              }

            //mStatusText.setText(R.string.signed_in);
            //mDetailText.setText(getString(R.string.firebase_status_fmt, user.getUid()));
        }
    }

    //This is just to check if the profile is already set or not
    //Currently we are checking it by firing query to firebase database, but to optimize this, we can store the states in configuration file locally
    public boolean checkInDatabase(FirebaseUser user) {


        try {
            databaseReference1 = firebaseDatabase.getReference().child("user").child(user.getUid());
            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();
                    if(map !=null){
                        Log.d("Inside Datasnapshot","1");
                     Set<String> key = map.keySet();
                    if (key.contains("email")) {
                        Log.d("Inside Datasnapshot","2");

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                    }else{
                        enableViews(mSave);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        return true;
    }

    //check that number field is not empty
    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || !phoneNumber.matches(String.valueOf(Patterns.PHONE))) {
            mPhoneNumberField.setError("Invalid phone number."); //if phone no is not entered show error
            return false;
        }

        return true;
    }

    //This funtion takes the array of views, and Enable them so that they can be accessed
    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    //This funtion takes the array of views, and disable them so that they cannot be accessed
    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }

    //This is called when a startActivityForResult() is called, to get the results from other activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //as this method can be called for diff purpose we use request code
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);

                //To store the image on Firebase Storage
                FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
                StorageReference riversRef = firebaseStorage.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_photos");

                riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Photo Uploaded",Toast.LENGTH_LONG).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //All the click events are handled in thins method
    @Override
    public void onClick(View view) {
        String user_phone_number=null;
        switch (view.getId()) {
            case R.id.button_start_verification:  //Code behind Start button
                if (!validatePhoneNumber()) {   //to check that field is not empty
                    return;
                }
                 user_phone_number=mPhoneNumberField.getText().toString();
                //if not empty, start the verification process, by passing the phone no
                startPhoneNumberVerification(user_phone_number);

                break;
            case R.id.button_save: //Code behind save button

                if(validateDetailsFields()){       //check that required fields are not empty
                    dialog.setMessage("Saving your profile");
                    dialog.show();  //starts a progress dialog, visible only if your internet speed is slow
                     setDetailInDatabse(); // call to a funtion which save the user data in Firebase database and Storage
                    dialog.dismiss(); // Close the progress dialog on completion
                    startActivity(new Intent(this,MainActivity.class));  // after setup of profile start the Main page of Map
                }
                break;
            case R.id.button_resend: // Code behind resend  button
                resendVerificationCode(user_phone_number, mResendToken); //if not received the verification code in 5 mins
                break;
            case R.id.sign_out_button: //code behind sign out button
                signOut();
                finish();
                break;
            case R.id.profile_image:  //code to behind the image view of profile image
                // Creating an implicit intent to get the image from gallery or camera
                //Implicit intents do not name a specific component, but instead
                // declare a general action to perform, which allows a component from another app to handle it.

                Intent intent = new Intent();
                intent.setType("image/*");  // represent the MIME type data that u want to get in return from firing intent
                intent.setAction(Intent.ACTION_GET_CONTENT); //allows to get the item of the specified MIME type

                //You can also start another activity and receive a result back.To receive a result, call startActivityForResult() ...
                //we need to override the onActivityResult method that is invoked automatically when second activity returns result.

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                break;
        }

    }

    //if validated successfully update the email and addess of user in database
    private void setDetailInDatabse() {
        try {
            databaseReference = firebaseDatabase.getReference().child("user").child(mAuth.getCurrentUser().getUid());
            databaseReference.child("saved_count").setValue(0);
            databaseReference.child("phoneNo").setValue(mAuth.getCurrentUser().getPhoneNumber());
            databaseReference.child("email").setValue(mEmailField.getText().toString());
            databaseReference.child("address").setValue(mAddressField.getText().toString());
        }catch (NullPointerException e){

        }
    }

    //Validate address and email
    private boolean validateDetailsFields() {

        if(mEmailField.getText().toString().isEmpty()){
            mEmailField.setError("Please enter email id");
            return false;
        }else{
            //using email validator of android
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailField.getText().toString()).matches()){
                mEmailField.setError("Invalid email id");
                return false;
            }
        }
        if(mAddressField.getText().toString().isEmpty()){
            mAddressField.setError("Please Enter your address");
            return  false;
        }

        return  true;
    }
}