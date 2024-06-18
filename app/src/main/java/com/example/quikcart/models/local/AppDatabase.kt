package com.example.quikcart.models.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.utils.Constants
import com.example.quikcart.utils.Converters

@Database(entities = [ProductsItem::class, PriceRule::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao():AppDao

    companion object{
        private var dbInstance:AppDatabase?=null
        fun getInstance(context:Context):AppDatabase{
            return dbInstance?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                .fallbackToDestructiveMigration() .build()
                dbInstance =instance
                instance
            }
        }
    }
}