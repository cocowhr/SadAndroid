package com.example.hospital;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.hospital.model.CheckValid;
import com.example.hospital.model.HospitalBarActivity;
import com.example.hospital.model.MyException;
import com.example.hospital.network.JsonPost;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends HospitalBarActivity implements LoaderCallbacks<Cursor> {

	/**
	 * A dummy authentication store containing known user names and passwords.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] { "foo@example.com:hello", "bar@example.com:world" };
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	private AutoCompleteTextView mUserNameView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;
	private final static int SIGN_IN = 1;
	Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		 * getWindow().addFlags(WindowManager.LayoutParams.
		 * FLAG_TRANSLUCENT_STATUS);
		 * getWindow().addFlags(WindowManager.LayoutParams.
		 * FLAG_TRANSLUCENT_NAVIGATION); }
		 */
		setContentView(R.layout.activity_login);
		try {
			// noinspection ConstantConditions
			getActionBar().setDisplayHomeAsUpEnabled(true);
		} catch (NullPointerException e) {
			//
		}

		// Set up the login form.
		// TODO:测试
		mUserNameView = (AutoCompleteTextView) findViewById(R.id.name_in_login);
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
		TextView register = (TextView) findViewById(R.id.register_button);
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				 toRegisterActivity(view);
			}
		});
		toast = new Toast(getApplicationContext());
	}

	private void populateAutoComplete() {
		getLoaderManager().initLoader(0, null, this);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUserNameView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String userName = mUserNameView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (password.isEmpty()) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (!CheckValid.isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;

		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(userName)) {
			mUserNameView.setError(getString(R.string.error_field_required));
			focusView = mUserNameView;
			cancel = true;

		} else if (!CheckValid.isUserNameValid(userName)) {
			mUserNameView.setError(getString(R.string.user_name_invalid));
			focusView = mUserNameView;
			cancel = true;

		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserLoginTask(userName, password);
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		return new CursorLoader(this,
				// Retrieve data rows for the device user's 'profile' contact.
				Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
						ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
				ProfileQuery.PROJECTION,

		// Select only email addresses.
				ContactsContract.Contacts.Data.MIMETYPE + " = ?",
				new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE },

		// Show primary email addresses first. Note that there won't be
		// a primary email address if the user hasn't specified one.
				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		List<String> emails = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			emails.add(cursor.getString(ProfileQuery.ADDRESS));
			cursor.moveToNext();
		}

		addEmailsToAutoComplete(emails);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {

	}

	private interface ProfileQuery {
		String[] PROJECTION = { ContactsContract.CommonDataKinds.Email.ADDRESS,
				ContactsContract.CommonDataKinds.Email.IS_PRIMARY, };

		int ADDRESS = 0;
		int IS_PRIMARY = 1;
	}

	private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
		// Create adapter to tell the AutoCompleteTextView what to show in its
		// dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoginActivity.this,
				android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

		mUserNameView.setAdapter(adapter);
	}

	 public void toRegisterActivity(View view) {
	
	 Intent intent = new Intent(this, RegisterActivity.class);
	 startActivity(intent);
	
	 }

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

		private final String mUserName;
		private final String mPassword;
		private int type;

		UserLoginTask(String name, String password) {
			mUserName = name;
			mPassword = password;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			Integer rettype = 0;
			try {
				String url = "http://172.31.131.20/api/loginandroid/";
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("username", mUserName);
				map.put("password", mPassword);
				JsonPost post = new JsonPost(map, url);
				Thread.sleep(500);
			} catch (MyException.passwordWrongException e) {
				rettype = 1;
			} catch (Exception e) {
				rettype = 2;
			}
			return rettype;
		}

		@Override
		protected void onPostExecute(final Integer rettype) {
			mAuthTask = null;
			showProgress(false);

			if (0 == rettype) {
				Toast.makeText(getApplicationContext(), "登录成功!", Toast.LENGTH_SHORT).show();
				// LoginState.setLogined(true, mUserName);
				finish();
			} else if (1 == rettype) {
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			} else {
				Toast.makeText(getApplicationContext(), "未知错误!", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}

	}
}