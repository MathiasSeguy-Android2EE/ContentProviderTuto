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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class ContentProviderTuto extends ContentProvider {
	/******************************************************************************************/
	/** Constants definition ******************************************************************/
	/******************************************************************************************/
	// Authority, path to data and URI
	/**
	 * The Authority
	 * use the package of the content provider class
	 */
	public static final String AUTHORITY = "com.android2ee.tuto.contentprovider.definition";
	/**
	 * The path to the human data …
	 * You can declare more than one path, use / to define deepest path
	 */
	public static final String PATH_TO_DATA = "Human";

	/** @goals This class aims to show the constant to use for the ContentProvider */
	public static class Constants implements BaseColumns {
		/** The URI and explain, with example if you want */
		public static final Uri CONTENT_URI = Uri.parse("content://" + ContentProviderTuto.AUTHORITY + "/"
				+ ContentProviderTuto.PATH_TO_DATA);
		// Mon MIME type
		/** My Column ID and the associated explanation */
		public static final String MIME_COLLECTION = "vnd.android.cursor.dir/vnd.android2ee.tuto.contentprovider.human";
		/** My Column ID and the associated explanation */
		public static final String MIME_ITEM = "vnd.android.cursor.item/vnd.android2ee.tuto.contentprovider.human";
		// Noms de colonnes
		// /!\Si vous utilisez une base de données, les noms des colonnes ont les mêmes que ceux de
		// votre bases, de même pour les indexes.
		/** My Column ID and the associated explanation for end-users */
		public static final String KEY_COL_ID = "_id";// Mandatory
		/** My Column Name and the associated explanation for end-users */
		public static final String KEY_COL_NAME = "name";
		/** My Column First Name and the associated explanation for end-users */
		public static final String KEY_COL_FIRSTNAME = "firstName";
		/** My Column Eyes Color and the associated explanation for end-users */
		public static final String KEY_COL_EYES_COLOR = "eyesColor";
		/** My Column Hair color and the associated explanation for end-users */
		public static final String KEY_COL_HAIR_COLOR = "hairColor";
		/** My Column age and the associated explanation for end-users */
		public static final String KEY_COL_AGE = "age";

		// Indexes des colonnes
		/** The index of the column ID */
		public static final int ID_COLUMN = 1;
		/** The index of the column NAME */
		public static final int NAME_COLUMN = 2;
		/** The index of the column FIRST NAME */
		public static final int FIRSTNAME_COLUMN = 3;
		/** The index of the column EYES COLOR */
		public static final int EYES_COLOR_COLUMN = 4;
		/** The index of the column HAIR COLOR */
		public static final int HAIR_COLOR_COLUMN = 5;
		/** The index of the column AGE */
		public static final int AGE_COLUMN = 6;
	}

	/******************************************************************************************/
	/** Attribute **************************************************************************/
	/******************************************************************************************/

	/** * The database */
	private SQLiteDatabase db;
	/** * The database creator and updater helper */
	DBOpenHelper dbOpenHelper;

	/******************************************************************************************/
	/** Initialization ******************************************************************/
	/******************************************************************************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		Context context = getContext();
		// Create or retrieve the database
		dbOpenHelper = new DBOpenHelper(context, DBOpenHelper.Constants.DATABASE_NAME, null,
				DBOpenHelper.Constants.DATABASE_VERSION);
		return (dbOpenHelper == null) ? false : true;
	}

	/**
	 * * Open the database in writeMode
	 * 
	 * @throws SQLiteException
	 */
	public void openDB() throws SQLiteException {
		db = dbOpenHelper.getWritableDatabase();
	}

	/**
	 * * Open the database*
	 * 
	 * @throws SQLiteException
	 */
	public void openRDB() throws SQLiteException {
		db = dbOpenHelper.getReadableDatabase();
	}

	/** *Close Database */
	public void closeDB() {
		db.close();
	}

	/******************************************************************************************/
	/** Type management **************************************************************************/
	/******************************************************************************************/
	// Create the constants to define the URI request type
	/**
	 * When the URI is an ITEM
	 */
	private static final int ITEM = 1;
	/**
	 * When the URI is a collection
	 */
	private static final int COLLECTION = 2;
	/**
	 * The uri matcher that calculate the type of the URI
	 */
	private static final UriMatcher uriMatcher;
	// Allocate the UriMatcher object
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// if the URI ends with myPathToData, then it's a collection
		uriMatcher.addURI(AUTHORITY, PATH_TO_DATA, COLLECTION);
		// else if the URI ends with a '/[rowID]', then it's an item
		uriMatcher.addURI(AUTHORITY, PATH_TO_DATA + "/#", ITEM);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case COLLECTION:
			return Constants.MIME_COLLECTION;
		case ITEM:
			return Constants.MIME_ITEM;
		default:
			throw new IllegalArgumentException("URI non supportée : " + uri);
		}
	}

	/******************************************************************************************/
	/** CRUD Methods ******************************************************************/
	/******************************************************************************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[],
	 * java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) {
		// instanciate the SQLiteQueryBuilder
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		// as there is just one table and one path there is no need to analyze uri to know which is
		// the table
		qb.setTables(DBOpenHelper.Constants.MY_TABLE);
		// if it's an item, then limit the result
		switch (uriMatcher.match(uri)) {
		case ITEM:
			qb.appendWhere(Constants.KEY_COL_ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			break;
		}
		// If no order defined, use the name to set order else use the order gave as parameter
		String orderBy;
		if (TextUtils.isEmpty(sort)) {
			orderBy = Constants.KEY_COL_NAME;
		} else {
			orderBy = sort;
		}
		// open the database in read mode
		openRDB();
		// Do the request in the database.
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		// Save the ContextResolver to notify it if the result changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		// keep the base open, else the garbage collector will garbage them
		// So don't close database
		// Return the cursor.
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri _uri, ContentValues _initialValues) {
		//When inserting data open and close the database
		// open the database
		openDB();
		// Insert the new line and return its number if the operation succeeds
		long rowID = db.insert(DBOpenHelper.Constants.MY_TABLE, "data", _initialValues);
		// Return the uri of the new line
		if (rowID > 0) {
			Uri uri = ContentUris.withAppendedId(Constants.CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			// close database
			closeDB();
			return uri;
		}
		// close database
				closeDB();
		// else throw an exception
		throw new SQLException("Echec de l'ajout d'une ligne dans " + _uri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String,
	 * java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// open the database
		openDB();
		// the number of delete elements
		int count;
		// switch depending on the uri, if it's a collection or an item
		switch (uriMatcher.match(uri)) {
		case COLLECTION:
			// do the deletion passing the parameters
			count = db.delete(DBOpenHelper.Constants.MY_TABLE, where, whereArgs);
			break;
		case ITEM:
			// the segment of the uri (should be the item id)
			String segment = uri.getPathSegments().get(1);
			// do the deletion specifying the item number (should be the number of the item's column
			// id)
			count = db.delete(DBOpenHelper.Constants.MY_TABLE, DBOpenHelper.Constants.KEY_COL_ID + "=" + segment
					+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		default:
			// close database
			closeDB();
			// else die
			throw new IllegalArgumentException("URI non supportée : " + uri);
		}
		// notify the change
		getContext().getContentResolver().notifyChange(uri, null);
		// close database
		closeDB();
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		// open the database
		openDB();
		// the number of updated rows
		int count;
		// switch depending on the uri, if it's a collection or an item
		switch (uriMatcher.match(uri)) {
		case COLLECTION:
			// do the update
			count = db.update(DBOpenHelper.Constants.MY_TABLE, values, where, whereArgs);
			break;
		case ITEM:
			// the segment of the uri (should be the item id)
			String segment = uri.getPathSegments().get(1);
			// do the update specifying the item number (should be the number of the item's column
			// id)
			count = db.update(DBOpenHelper.Constants.MY_TABLE, values, Constants.KEY_COL_ID + "=" + segment
					+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		default:
			// close database
			closeDB();
			// just die in a big scream
			throw new IllegalArgumentException("URI inconnue " + uri);
		}
		// notify ContentResolver of the change
		getContext().getContentResolver().notifyChange(uri, null);
		// close database
		closeDB();
		// return the number of updated rows
		return count;
	}

}
