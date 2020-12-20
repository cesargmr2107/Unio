package org.uvigo.esei.unio.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SQLManager extends SQLiteOpenHelper {

    private static final String DB_NAME = "ServiceMessages";
    private static final int DB_VERSION = 3;

    public enum ServiceTable {
        MailService,
        WeatherService,
        TranslationService,
        NotesService,
        CalculatorService
    }

    public static final String ID_FIELD = "_id";
    public static final String TEXT_FIELD = "text";
    public static final String TYPE_FIELD = "type";

    public SQLManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.i(DB_NAME, "Creating DB and its tables");

            db.beginTransaction();
            for (ServiceTable s : ServiceTable.values()) {
                db.execSQL("CREATE TABLE IF NOT EXISTS " + s.toString()
                        + "("
                        + ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + TEXT_FIELD + " TEXT NOT NULL,"
                        + TYPE_FIELD + " TEXT NOT NULL"
                        + ")"
                );
            }
            db.setTransactionSuccessful();

        } catch (SQLException error) {
            Log.e(DB_NAME, "ERROR: " + error.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.i(DB_NAME, "Upgrading DB...");

            db.beginTransaction();
            for (ServiceTable s : ServiceTable.values()) {
                db.execSQL("DROP TABLE IF EXISTS " + s.toString());
            }
            db.setTransactionSuccessful();

        } catch (SQLException error) {
            Log.e(DB_NAME, "ERROR: " + error.getMessage());
        } finally {
            db.endTransaction();
        }

        this.onCreate(db);
    }

    public List<Message> getMessages(String serviceTable) {
        final SQLiteDatabase DB = this.getReadableDatabase();
        List<Message> messages = new ArrayList<>();
        Cursor cursor = DB.query(
                serviceTable,
                null,
                null,
                null,
                null,
                null,
                ID_FIELD
        );

        final int MSG_ID_INDEX = cursor.getColumnIndex(ID_FIELD);
        final int MSG_TEXT_INDEX = cursor.getColumnIndex(TEXT_FIELD);
        final int MSG_TYPE_INDEX = cursor.getColumnIndex(TYPE_FIELD);

        if (!cursor.moveToFirst()) {
            cursor.close();
            DB.close();
            return messages; // Empty message list
        }

        do {
            int id = cursor.getInt(MSG_ID_INDEX);
            String text = cursor.getString(MSG_TEXT_INDEX);
            Message.Type type = Message.Type.valueOf(cursor.getString(MSG_TYPE_INDEX));
            messages.add(new Message(serviceTable, id, type, text));
        } while (cursor.moveToNext());

        cursor.close();
        DB.close();

        return messages;
    }

    public String getLastMessage(String serviceTable) {
        final SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.query(
                serviceTable,
                new String[]{TEXT_FIELD},
                null,
                null,
                null,
                null,
                ID_FIELD
        );
        String msg;
        if (cursor.moveToLast()) {
            final int MSG_TEXT_INDEX = cursor.getColumnIndex(TEXT_FIELD);
            msg = cursor.getString(MSG_TEXT_INDEX);
        } else {
            msg = "";
        }
        cursor.close();
        DB.close();
        return msg;
    }

    public void addMessage(String serviceTable, Message message) {

        final SQLiteDatabase DB = this.getWritableDatabase();
        final ContentValues VALUES = new ContentValues();

        VALUES.put(TEXT_FIELD, message.getText());
        VALUES.put(TYPE_FIELD, message.getType().toString());

        try {
            DB.beginTransaction();

            DB.insert(
                    serviceTable,
                    null,
                    VALUES
            );

            DB.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e(DB_NAME, "ERROR: " + error.getMessage());
        } finally {
            DB.endTransaction();
        }
    }

    public void deleteMessage(String serviceTable, int messageId) {
        final SQLiteDatabase DB = this.getWritableDatabase();

        try {
            DB.beginTransaction();

            DB.delete(serviceTable,
                    ID_FIELD + " = ?",
                    new String[]{messageId + ""});

            DB.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e(DB_NAME, "ERROR: " + error.getMessage());
        } finally {
            DB.endTransaction();
        }
    }

    public void deleteAllMessages(String serviceTable) {
        final SQLiteDatabase DB = this.getWritableDatabase();
        try {
            DB.beginTransaction();

            DB.delete(serviceTable, null, null);

            DB.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e(DB_NAME, "ERROR: " + error.getMessage());
        } finally {
            DB.endTransaction();
        }
    }

    public void deleteAllMessages() {
        final SQLiteDatabase DB = this.getWritableDatabase();
        try {
            DB.beginTransaction();

            for (ServiceTable serviceTable : ServiceTable.values()) {
                DB.delete(serviceTable.name(), null, null);
            }

            DB.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e(DB_NAME, "ERROR: " + error.getMessage());
        } finally {
            DB.endTransaction();
        }
    }
}
