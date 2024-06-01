package das.mobile.storyapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import das.mobile.storyapp.data.api.response.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(quote: List<Story>)

    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int, Story>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}