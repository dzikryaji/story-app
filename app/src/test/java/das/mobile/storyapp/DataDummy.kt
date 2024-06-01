package das.mobile.storyapp

import das.mobile.storyapp.data.api.response.Story
import das.mobile.storyapp.data.api.response.StoryResponse

object DataDummy {
    fun generateDummyStoryResponse(): StoryResponse {
        val listStory: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val quote = Story(
                i.toString(),
                "https://story-api.dicoding.dev/images/stories/photos-1716370726397_bf382d067fa5574f4858.jpg",
                "2024-05-22T09:38:46.398Z",
                "author $i",
                "lorem ipsum $i",
                0.0,
                0.0
            )
            listStory.add(quote)
        }
        val storyResponse = StoryResponse(
            false,
            "Stories fetched successfully",
            listStory
        )
        return storyResponse
    }
}