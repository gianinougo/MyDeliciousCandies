package com.ugogianino.mydeliciouscandies.adapters

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import com.ugogianino.mydeliciouscandies.model.Candie
import com.ugogianino.mydeliciouscandies.model.CandyType
import com.ugogianino.mydeliciouscandies.model.Format
import com.ugogianino.mydeliciouscandies.model.Manufacturer

class MyDeliciousCandiesDBAdapter(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private var instance: MyDeliciousCandiesDBAdapter? = null

        fun getInstance(context: Context): MyDeliciousCandiesDBAdapter {
            if (instance == null) {
                instance = MyDeliciousCandiesDBAdapter(context)
            }
            return instance!!
        }

            private const val DATABASE_NAME = "mydeliciouscandies.db"
            private const val DATABASE_VERSION = 1
            private const val TABLE_CANDIES = "candies"
            private const val TABLE_MANUFACTURER = "manufacturer"
            private const val TABLE_CANDY_TYPE = "candy_type"
            private const val TABLE_FORMAT = "format"

            private const val COL_CANDIES_ID = "id"
            private const val COL_CANDIES_NAME = "name"
            private const val COL_CANDIES_MANUFACTURER_ID = "manufacturer_id"
            private const val COL_CANDIES_CANDY_TYPE_ID = "candy_type_id"
            private const val COL_CANDIES_FORMAT_ID = "format_id"
            private const val COL_CANDIES_SWEETNESS = "sweetness"
            private const val COL_CANDIES_IMAGE = "image"
            private const val COL_CANDIES_URL = "url"
            private const val COL_CANDIES_IS_FAVORITE = "is_favorite"
            private const val COL_CANDIES_EXPIRATION_DATE = "expiration_date"

            private const val COL_MANUFACTURER_ID = "id"
            private const val COL_MANUFACTURER_NAME = "name"
            private const val COL_MANUFACTURER_WEBSITE = "website"

            private const val COL_CANDY_TYPE_ID = "id"
            private const val COL_CANDY_TYPE_NAME = "name"

            private const val COL_FORMAT_ID = "id"
            private const val COL_FORMAT_NAME = "name"

    }



    override fun onCreate(db: SQLiteDatabase?) {
        val createCandiesTable = "CREATE TABLE ${TABLE_CANDIES} " +
                "(${COL_CANDIES_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${COL_CANDIES_NAME} TEXT, " +
                "${COL_CANDIES_MANUFACTURER_ID} INTEGER, " +
                "${COL_CANDIES_CANDY_TYPE_ID} INTEGER, " +
                "${COL_CANDIES_FORMAT_ID} INTEGER, " +
                "${COL_CANDIES_SWEETNESS} INTEGER, " +
                "${COL_CANDIES_IMAGE} BLOB, " +
                "${COL_CANDIES_URL} TEXT, " +
                "${COL_CANDIES_IS_FAVORITE} INTEGER, " +
                "${COL_CANDIES_EXPIRATION_DATE} DATE);"
        db?.execSQL(createCandiesTable)

        val createManufacturerTable = "CREATE TABLE ${TABLE_MANUFACTURER} " +
                "(${COL_MANUFACTURER_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${COL_MANUFACTURER_NAME} TEXT, " +
                "${COL_MANUFACTURER_WEBSITE} TEXT);"
        db?.execSQL(createManufacturerTable)

        val createCandyTypeTable = "CREATE TABLE ${TABLE_CANDY_TYPE} " +
                "(${COL_CANDY_TYPE_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${COL_CANDY_TYPE_NAME} TEXT);"
        db?.execSQL(createCandyTypeTable)

        val createFormatTable = "CREATE TABLE ${TABLE_FORMAT} " +
                "(${COL_FORMAT_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${COL_FORMAT_NAME} TEXT);"
        db?.execSQL(createFormatTable)
    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CANDIES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FORMAT")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MANUFACTURER")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CANDY_TYPE")
        onCreate(db)
    }


    fun addCandie(candie: Candie) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_CANDIES_NAME, candie.name)
            put(COL_CANDIES_MANUFACTURER_ID, candie.manufacturer.id)
            put(COL_CANDIES_CANDY_TYPE_ID, candie.candyType.id)
            put(COL_CANDIES_FORMAT_ID, candie.format.id)
            put(COL_CANDIES_SWEETNESS, candie.sweetness)
            put(COL_CANDIES_IMAGE, candie.image)
            put(COL_CANDIES_URL, candie.url)
            put(COL_CANDIES_IS_FAVORITE, candie.isFavourite)
        }
        val newRowId = db?.insert(TABLE_CANDIES, null, values)
        if (newRowId == -1L) {
            // Fallo al insertar la nueva fila
        }
    }



    fun updateCandie(candie: Candie) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_CANDIES_NAME, candie.name)
            put(COL_CANDIES_MANUFACTURER_ID, candie.manufacturer.id)
            put(COL_CANDIES_CANDY_TYPE_ID, candie.candyType.id)
            put(COL_CANDIES_FORMAT_ID, candie.format.id)
            put(COL_CANDIES_SWEETNESS, candie.sweetness)
            put(COL_CANDIES_IMAGE, candie.image)
            put(COL_CANDIES_URL, candie.url)
            put(COL_CANDIES_IS_FAVORITE, candie.isFavourite)
        }
        val selection = "$COL_CANDIES_ID = ?"
        val selectionArgs = arrayOf(candie.id.toString())

        db.update(TABLE_CANDIES, values, selection, selectionArgs)
    }

    fun deleteCandie(candieId: Int) {
        val db = writableDatabase
        db.delete(TABLE_CANDIES, "$COL_CANDIES_ID = ?", arrayOf(candieId.toString()))
        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllCandies(): List<Candie> {
        val candies = mutableListOf<Candie>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CANDIES", null)
        cursor.use {
            while (it.moveToNext()) {
                val candie = cursorToCandie(it)
                candies.add(candie)
            }
        }
        return candies
    }
    @SuppressLint("Range")
    fun getManufacturerId(manufacturerName: String): Int {
        val db = writableDatabase
        val query = "SELECT $COL_MANUFACTURER_ID FROM $TABLE_MANUFACTURER WHERE $COL_MANUFACTURER_NAME = '$manufacturerName'"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()){
            val manufacturerId = cursor.getInt(0)
            cursor.close()
            return manufacturerId
        }else{
            println("Cursor is empty, no manufacturer found with name $manufacturerName")
            cursor.close()
            return -1
        }
    }


    fun getCandyTypeId(candyTypeName: String): Int {
        val db = writableDatabase
        val query = "SELECT ${COL_CANDY_TYPE_ID} FROM ${TABLE_CANDY_TYPE} WHERE ${COL_CANDY_TYPE_NAME} = '$candyTypeName'"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val candyTypeId = cursor.getInt(0)
        cursor.close()
        return candyTypeId
    }

    fun getFormatId(formatName: String): Int {
        val db = writableDatabase
        val query = "SELECT ${COL_FORMAT_ID} FROM ${TABLE_FORMAT} WHERE ${COL_FORMAT_NAME} = '$formatName'"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val formatId = cursor.getInt(0)
        cursor.close()
        return formatId
    }



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    fun cursorToCandie(cursor: Cursor): Candie {
        val id = cursor.getInt(cursor.getColumnIndex(COL_CANDIES_ID))
        val name = cursor.getString(cursor.getColumnIndex(COL_CANDIES_NAME))
        val manufacturer = getManufacturer(cursor.getInt(cursor.getColumnIndex(COL_CANDIES_MANUFACTURER_ID)))
        val candyType = getCandyType(cursor.getInt(cursor.getColumnIndex(COL_CANDIES_CANDY_TYPE_ID)))
        val format = getFormat(cursor.getInt(cursor.getColumnIndex(COL_CANDIES_FORMAT_ID)))
        val sweetness = cursor.getInt(cursor.getColumnIndex(COL_CANDIES_SWEETNESS))
        val image = cursor.getBlob(cursor.getColumnIndex(COL_CANDIES_IMAGE))
        val url = cursor.getString(cursor.getColumnIndex(COL_CANDIES_URL))
        val isFavourite = cursor.getInt(cursor.getColumnIndex(COL_CANDIES_IS_FAVORITE)) == 1
        return Candie(id, name, manufacturer, candyType, format, sweetness, image, url, isFavourite)
    }


    @SuppressLint("Range")
    private fun getFormat(int: Int): Format {
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_FORMAT WHERE $COL_FORMAT_ID = $int", null)
        cursor.moveToFirst()
        val id = cursor.getInt(cursor.getColumnIndex(COL_FORMAT_ID))
        val name = cursor.getString(cursor.getColumnIndex(COL_FORMAT_NAME))
        cursor.close()
        return Format(id, name)
    }

    @SuppressLint("Range")
    private fun getCandyType(int: Int): CandyType {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CANDY_TYPE,
            arrayOf(COL_CANDY_TYPE_ID, COL_CANDY_TYPE_NAME),
            "$COL_CANDY_TYPE_ID = ?",
            arrayOf(int.toString()),
            null,
            null,
            null
        )
        cursor.moveToFirst()
        val candyType = CandyType(
            cursor.getInt(cursor.getColumnIndex(COL_CANDY_TYPE_ID)),
            cursor.getString(cursor.getColumnIndex(COL_CANDY_TYPE_NAME))
        )
        cursor.close()
        return candyType
    }

    @SuppressLint("Range")
    private fun getManufacturer(id: Int): Manufacturer {
        val db = readableDatabase
        val cursor = db.query(TABLE_MANUFACTURER, null, "$COL_MANUFACTURER_ID = ?", arrayOf(id.toString()), null, null, null)
        cursor.moveToFirst()
        val name = cursor.getString(cursor.getColumnIndex(COL_MANUFACTURER_NAME))
        val website = cursor.getString(cursor.getColumnIndex(COL_MANUFACTURER_WEBSITE))
        cursor.close()
        return Manufacturer(id, name, website)
    }



    fun insertInitialData() {
        val db = writableDatabase
        db.beginTransaction()
        try {
            // Insertar datos en la tabla de fabricantes
            val manufacturer1 = ContentValues()
            manufacturer1.put(COL_MANUFACTURER_NAME, "Fabricante 1")
            manufacturer1.put(COL_MANUFACTURER_WEBSITE, "http://fabricante1.com")
            db.insert(TABLE_MANUFACTURER, null, manufacturer1)

            val manufacturer2 = ContentValues()
            manufacturer2.put(COL_MANUFACTURER_NAME, "Fabricante 2")
            manufacturer2.put(COL_MANUFACTURER_WEBSITE, "http://fabricante2.com")
            db.insert(TABLE_MANUFACTURER, null, manufacturer2)

            // Insertar datos en la tabla de tipos de caramelos
            val candyType1 = ContentValues()
            candyType1.put(COL_CANDY_TYPE_NAME, "Tipo 1")
            db.insert(TABLE_CANDY_TYPE, null, candyType1)

            val candyType2 = ContentValues()
            candyType2.put(COL_CANDY_TYPE_NAME, "Tipo 2")
            db.insert(TABLE_CANDY_TYPE, null, candyType2)

            // Insert ar datos en la tabla de formatos

            val format1 = ContentValues()
            format1.put(COL_FORMAT_NAME, "Formato 1")
            db.insert(TABLE_FORMAT, null, format1)
            val format2 = ContentValues()
            format2.put(COL_FORMAT_NAME, "Formato 2")
            db.insert(TABLE_FORMAT, null, format2)

            // Insertar datos en la tabla de caramelos
            val cand1 = ContentValues()
            cand1.put(COL_CANDIES_NAME, "Caramelo 1")
            cand1.put(COL_CANDIES_MANUFACTURER_ID, 1)
            cand1.put(COL_CANDIES_CANDY_TYPE_ID, 1)
            cand1.put(COL_CANDIES_FORMAT_ID, 1)
            cand1.put(COL_CANDIES_SWEETNESS, 5)
            cand1.put(COL_CANDIES_IMAGE, "")
            cand1.put(COL_CANDIES_URL, "http://caramelo1.com")
            cand1.put(COL_CANDIES_IS_FAVORITE, 0)

            db.insert(TABLE_CANDIES, null, cand1)

            val cand2 = ContentValues()
            cand2.put(COL_CANDIES_NAME, "Caramelo 2")
            cand2.put(COL_CANDIES_MANUFACTURER_ID, 2)
            cand2.put(COL_CANDIES_CANDY_TYPE_ID, 2)
            cand2.put(COL_CANDIES_FORMAT_ID, 2)
            cand2.put(COL_CANDIES_SWEETNESS, 3)
            cand2.put(COL_CANDIES_IMAGE, "")
            cand2.put(COL_CANDIES_URL, "http://caramelo2.com")
            cand2.put(COL_CANDIES_IS_FAVORITE, 1)

            db.insert(TABLE_CANDIES, null, cand2)

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            throw e
        }

        db.endTransaction()
    }


}


