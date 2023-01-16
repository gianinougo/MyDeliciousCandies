package com.ugogianino.mydeliciouscandies.adapters

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ugogianino.mydeliciouscandies.model.Candie

class MyDeliciousCandiesDBAdapter(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "candies.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_CANDIES = "candies"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_MANUFACTURER = "manufacturer"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_FORMAT = "format"
        private const val COLUMN_SWEETNESS = "sweetness"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_URL = "url"
        private const val COLUMN_FAVORITE = "favorite"
    }



    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_CANDIES ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME TEXT, $COLUMN_MANUFACTURER TEXT, $COLUMN_TYPE TEXT, $COLUMN_FORMAT TEXT, $COLUMN_IMAGE BLOB, $COLUMN_URL TEXT, $COLUMN_SWEETNESS INTEGER, $COLUMN_FAVORITE INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CANDIES")
        onCreate(db)
    }

    fun addCandie(candie: Candie) {
        val cv = ContentValues()
        cv.put(COLUMN_NAME, candie.name)
        cv.put(COLUMN_MANUFACTURER, candie.manufacturer)
        cv.put(COLUMN_TYPE, candie.candyType)
        cv.put(COLUMN_FORMAT, candie.saleFormat)
        cv.put(COLUMN_SWEETNESS, candie.sweetness)
        cv.put(COLUMN_IMAGE, candie.image)
        cv.put(COLUMN_URL, candie.url)
        cv.put(COLUMN_FAVORITE, if (candie.isFavourite) 1 else 0)
        writableDatabase.insert(TABLE_CANDIES, null, cv)
    }

    fun updateCandie(candie: Candie) {
        val cv = ContentValues()
        cv.put(COLUMN_NAME, candie.name)
        cv.put(COLUMN_MANUFACTURER, candie.manufacturer)
        cv.put(COLUMN_TYPE, candie.candyType)
        cv.put(COLUMN_FORMAT, candie.saleFormat)
        cv.put(COLUMN_SWEETNESS, candie.sweetness)
        cv.put(COLUMN_IMAGE, candie.image)
        cv.put(COLUMN_URL, candie.url)
        cv.put(COLUMN_FAVORITE, if (candie.isFavourite) 1 else 0)
        writableDatabase.update(TABLE_CANDIES, cv, "$COLUMN_ID = ?", arrayOf(candie.id.toString()))
    }

    fun deleteCandie(candie: Int) {
        writableDatabase.delete(TABLE_CANDIES, "$COLUMN_ID = ?", arrayOf(candie.toString()))
    }

    @SuppressLint("Range")
    fun getAllCandies(): List<Candie> {
        val candList = arrayListOf<Candie>()
        val cursor = readableDatabase.query(TABLE_CANDIES, null, null, null, null, null, null)
        if (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val manufacturer = cursor.getString(cursor.getColumnIndex(COLUMN_MANUFACTURER))
            val type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE))
            val format = cursor.getString(cursor.getColumnIndex(COLUMN_FORMAT))
            val image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE))
            val sweetness = cursor.getInt(cursor.getColumnIndex(COLUMN_SWEETNESS))
            val url = cursor.getString(cursor.getColumnIndex(COLUMN_URL))
            val favorite = cursor.getInt(cursor.getColumnIndex(COLUMN_FAVORITE)) == 1
            val cand = Candie(id, name, manufacturer, type, format, sweetness ,image, url, favorite)
            candList.add(cand)
        }
        cursor.close()
        return candList
    }




}

