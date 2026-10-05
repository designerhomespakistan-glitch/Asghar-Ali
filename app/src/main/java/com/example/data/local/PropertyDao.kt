package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.LeadSubmission
import com.example.data.model.PropertyListing
import com.example.data.model.SavedFavorite
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
    @Query("SELECT * FROM property_listings ORDER BY createdAt DESC")
    fun getAllProperties(): Flow<List<PropertyListing>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperties(properties: List<PropertyListing>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: PropertyListing)

    @Query("SELECT * FROM property_listings WHERE id = :id LIMIT 1")
    suspend fun getPropertyById(id: String): PropertyListing?

    @Query("DELETE FROM property_listings WHERE id = :id")
    suspend fun deletePropertyById(id: String)

    // Leads & Inquiries
    @Query("SELECT * FROM lead_submissions ORDER BY createdAt DESC")
    fun getAllLeads(): Flow<List<LeadSubmission>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLead(lead: LeadSubmission): Long

    @Query("DELETE FROM lead_submissions WHERE id = :id")
    suspend fun deleteLeadById(id: Long)

    // Favorites
    @Query("SELECT * FROM saved_favorites")
    fun getAllFavorites(): Flow<List<SavedFavorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: SavedFavorite)

    @Query("DELETE FROM saved_favorites WHERE listingId = :listingId")
    suspend fun removeFavorite(listingId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_favorites WHERE listingId = :listingId)")
    fun isFavorite(listingId: String): Flow<Boolean>
}
