/**<ul>
 * <li>ContentProviderTuto</li>
 * <li>com.android2ee.tuto.contentprovider.definition</li>
 * <li>21 déc. 2011</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage but can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * 
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
package com.android2ee.tuto.contentprovider.definition;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.android2ee.tuto.contentprovider.definition.DBOpenHelper.Constants;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class ContentProviderTutoActivity extends Activity {
	/**
	 * The URI of the Content provider ContentProviderTuto
	 */
	Uri myUri = ContentProviderTuto.Constants.CONTENT_URI;
	/**
	 * The text view that displays the operations
	 */
	TextView txv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		txv = (TextView) findViewById(R.id.txvHello);
		StringBuilder message = new StringBuilder(txv.getText());
		String alldata = readAll();
		message.append("\r\nInitial Database state\r\n");
		message.append((alldata == null) ? "Read failure" : alldata);
		txv.setText(message.toString());
		/**
		 * Data insertion
		 */
		// insert data
		ContentValues[] contentValues = getContentValues(1);
		Uri insertedUri = insertOneRow(contentValues[0]);
		// update GUI
		message.append("\r\n\r\n");
		message.append((insertedUri == null) ? "Insert Row KO failure" : "Insert Row Ok : " + insertedUri);
		txv.setText(message.toString());
		// insert a set of data
		contentValues = getContentValues(5);
		int insertedRowsNum = insertRows(contentValues);
		// update GUI
		message.append("\r\n\r\n");
		message.append((insertedRowsNum != 5) ? "Insert Rows KO failure" : "Insert Row Ok : " + insertedRowsNum);
		txv.setText(message.toString());
		/**
		 * Data reading
		 */
		String data = read("blond", "green");
		message.append("\r\n\r\n");
		message.append((data == null) ? "Read failure" : data);
		txv.setText(message.toString());
		/**
		 * Update
		 */
		// update one row
		int updatedRow = updateOneRow(insertedUri);
		// update GUI
		message.append("\r\n\r\n");
		message.append((updatedRow != 1) ? "Updated Rows KO failure" : "Updated Row Ok : " + insertedRowsNum);
		txv.setText(message.toString());
		// check the changes are done
		data = read("blond", "blue");
		message.append("\r\n\r\n");
		message.append((data == null) ? "Read failure" : data);
		txv.setText(message.toString());

		// update a set of row
		int updatedRows = updateRows();
		message.append((updatedRows == 0) ? "Updated Rows KO failure " + insertedRowsNum : "Updated Row Ok : "
				+ insertedRowsNum);
		txv.setText(message.toString());
		// check the changes are done
		data = read("brown", "green");
		message.append("\r\n\r\n");
		message.append((data == null) ? "Read failure" : data);
		txv.setText(message.toString());

		/**
		 * Deletion
		 */
		// delete one element using its uri
		int deletedRow = delete(insertedUri);
		message.append((deletedRow == 0) ? "Deletion Rows KO failure " + insertedRowsNum : "Deletion Row Ok : "
				+ insertedRowsNum);
		txv.setText(message.toString());

		// delete all elements
		deleteAll();
		// check the changes are done
		data = readAll();
		message.append("\r\n\r\n");
		message.append((data == null) ? "Read failure" : data);
		txv.setText(message.toString());
	}

	/**
	 * @param arraySize
	 * @return
	 */
	private ContentValues[] getContentValues(int arraySize) {
		ContentValues[] contentValuesArray = new ContentValues[arraySize];
		String rand = ((Double) Math.ceil(Math.random() * 100)).toString();
		// fill the array with fake data, it's a tuto
		for (int i = 0; i < arraySize; i++) {
			contentValuesArray[i] = new ContentValues();
			contentValuesArray[i].put(Constants.KEY_COL_NAME, "name" + rand + ":" + i);
			contentValuesArray[i].put(Constants.KEY_COL_FIRSTNAME, "firstName" + rand + ":" + i);
			contentValuesArray[i].put(Constants.KEY_COL_EYES_COLOR, "green");
			contentValuesArray[i].put(Constants.KEY_COL_HAIR_COLOR, "blond");
			contentValuesArray[i].put(Constants.KEY_COL_AGE, i * 2);
		}

		return contentValuesArray;
	}

	/******************************************************************************************/
	/** Insertion **************************************************************************/
	/******************************************************************************************/
	/**
	 * Insert a Row
	 * 
	 * @param values
	 *            the content value to insert
	 * @return the URI of the inserted value
	 */
	private Uri insertOneRow(ContentValues values) {
		ContentResolver contentResolver = getContentResolver();

		ContentValues contentValuesArray = new ContentValues();
		contentValuesArray = new ContentValues();
		contentValuesArray.put(Constants.KEY_COL_NAME, "Smith");
		contentValuesArray.put(Constants.KEY_COL_FIRSTNAME, "Jonathan");
		contentValuesArray.put(Constants.KEY_COL_EYES_COLOR, "green");
		contentValuesArray.put(Constants.KEY_COL_HAIR_COLOR, "blond");
		contentValuesArray.put(Constants.KEY_COL_AGE, 26);

		Uri insertUri = contentResolver.insert(myUri, values);
		return insertUri;
	}

	/**
	 * Insert a set of data (rows)
	 * 
	 * @param values
	 *            the ContentValues to insert
	 * @return the number of inserted values
	 */
	private int insertRows(ContentValues[] values) {
		ContentResolver contentResolver = getContentResolver();
		int insertedRows = contentResolver.bulkInsert(myUri, values);
		return insertedRows;
	}

	/******************************************************************************************/
	/** Read **************************************************************************/
	/******************************************************************************************/

	/**
	 * Read the database where the haircolor==hairColor and eyeColor=eyeColor
	 * 
	 * @return
	 */
	private String read(String hairColor, String eyesColor) {
		// First define the parameters of the query
		// What are the column we want to get
		String[] projection = { ContentProviderTuto.Constants.KEY_COL_ID, ContentProviderTuto.Constants.KEY_COL_NAME,
				ContentProviderTuto.Constants.KEY_COL_FIRSTNAME, ContentProviderTuto.Constants.KEY_COL_AGE,
				ContentProviderTuto.Constants.KEY_COL_EYES_COLOR, ContentProviderTuto.Constants.KEY_COL_HAIR_COLOR };
		// What is the where clause?
		String selection = ContentProviderTuto.Constants.KEY_COL_EYES_COLOR + "=? AND "
				+ ContentProviderTuto.Constants.KEY_COL_HAIR_COLOR + "=?";
		// what are the parameters of the where clause?
		String[] selectionArgs = { eyesColor, hairColor };
		// How to sort elements
		String sortOrder = ContentProviderTuto.Constants.KEY_COL_AGE + " ASC";
		// then retrieve the content resolver
		ContentResolver contentResolver = getContentResolver();
		// then do the query
		Cursor cursor = contentResolver.query(myUri, projection, selection, selectionArgs, sortOrder);
		// The string to display the data
		StringBuilder data = new StringBuilder("\r\nData in Base where " + hairColor + "," + eyesColor + ":\r\n");
		// browse the returned elements
		while (cursor.moveToNext()) {
			// build the string that display elements
			data.append("id : " + cursor.getString(0) + ",\r\n");
			data.append("name : " + cursor.getString(1) + ",\r\n");
			data.append("firstname : " + cursor.getString(2) + ",\r\n");
			data.append("age : " + cursor.getString(3) + ",\r\n");
			data.append("eyes color : " + cursor.getString(4) + ",\r\n");
			data.append("hair color : " + cursor.getString(5) + ",\r\n");
			data.append("**********************************\r\n");
		}
		// BE SURE TO CLOSE THE CURSORS (else it runs a memory leaks)
		cursor.close();
		data.append("Data in Base were all displayed\r\n");
		return data.toString();
	}

	private String readAll() {
		// First define the parameters of the query
		// What are the column we want to get
		String[] projection = { ContentProviderTuto.Constants.KEY_COL_ID, ContentProviderTuto.Constants.KEY_COL_NAME,
				ContentProviderTuto.Constants.KEY_COL_FIRSTNAME, ContentProviderTuto.Constants.KEY_COL_AGE,
				ContentProviderTuto.Constants.KEY_COL_EYES_COLOR, ContentProviderTuto.Constants.KEY_COL_HAIR_COLOR };

		// How to sort elements
		String sortOrder = ContentProviderTuto.Constants.KEY_COL_AGE + " ASC";
		// then retrieve the content resolver
		ContentResolver contentResolver = getContentResolver();
		// then do the query
		Cursor cursor = contentResolver.query(myUri, projection, null, null, sortOrder);
		// The string to display the data
		StringBuilder data = new StringBuilder("\r\nData in Base :\r\n");
		// browse the returned elements
		while (cursor.moveToNext()) {
			// build the string that display elements
			data.append("id : " + cursor.getString(0) + ",\r\n");
			data.append("name : " + cursor.getString(1) + ",\r\n");
			data.append("firstname : " + cursor.getString(2) + ",\r\n");
			data.append("age : " + cursor.getString(3) + ",\r\n");
			data.append("eyes color : " + cursor.getString(4) + ",\r\n");
			data.append("hair color : " + cursor.getString(5) + ",\r\n");
			data.append("**********************************\r\n");
		}
		// BE SURE TO CLOSE THE CURSORS (else it runs a memory leaks)
		cursor.close();
		data.append("Data in Base were all displayed\r\n");
		return data.toString();
	}

	/******************************************************************************************/
	/** Update **************************************************************************/
	/******************************************************************************************/

	/**
	 * Update one row
	 * 
	 * @param uri
	 *            that point to an item
	 * @return the number of updated row
	 */
	private int updateOneRow(Uri uri) {
		// then retrieve the content resolver
		ContentResolver contentResolver = getContentResolver();
		// Create a new line:
		ContentValues contentValues = new ContentValues();
		contentValues.put(Constants.KEY_COL_EYES_COLOR, "blue");
		// then do the query
		int updatedRow = contentResolver.update(uri, contentValues, null, null);
		return updatedRow;
	}

	/**
	 * Update a set of rows
	 * 
	 * @return the number of updated rows
	 */
	private int updateRows() {
		// then retrieve the content resolver
		ContentResolver contentResolver = getContentResolver();
		// Create a new line:
		ContentValues contentValues = new ContentValues();
		contentValues.put(Constants.KEY_COL_EYES_COLOR, "green");
		contentValues.put(Constants.KEY_COL_HAIR_COLOR, "brown");
		// What is the where clause?
		String selection = ContentProviderTuto.Constants.KEY_COL_EYES_COLOR + "=? AND "
				+ ContentProviderTuto.Constants.KEY_COL_HAIR_COLOR + "=?";
		// what are the parameters of the where clause?
		String[] selectionArgs = { "green", "blond" };
		// then do the query
		int updatedRow = contentResolver.update(myUri, contentValues, selection, selectionArgs);
		return updatedRow;
	}

	/******************************************************************************************/
	/** Deletion **************************************************************************/
	/******************************************************************************************/
	/**
	 * @return
	 */
	private int delete(Uri uri) {
		// then retrieve the content resolver
		ContentResolver contentResolver = getContentResolver();
		int deletedRow = contentResolver.delete(uri, null, null);
		return deletedRow;
	}

	/**
	 * @return
	 */
	private int deleteAll() {
		// then retrieve the content resolver
		ContentResolver contentResolver = getContentResolver();
		contentResolver.delete(myUri, null, null);
		return 0;
	}
}