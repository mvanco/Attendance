package eu.matoosh.attendance.repo

import eu.matoosh.attendance.di.IoDispatcher
import eu.matoosh.attendance.api.SeznamService
import eu.matoosh.attendance.data.Book
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface RepoBooksResponse {
    data class Success(val books: List<Book>) : RepoBooksResponse
    data class Error(val code: Int) : RepoBooksResponse
}

sealed interface RepoBookDetailResponse {
    data class Success(val bookDetail: Book) : RepoBookDetailResponse
    data class Error(val code: Int) : RepoBookDetailResponse
}

class BookRepository @Inject constructor(
    private val service: SeznamService,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend fun getBooks(author: String): RepoBooksResponse = withContext(defaultDispatcher) {
        val response = service.getBooks("inauthor:$author")
        if (response.error != null) {
            RepoBooksResponse.Error(response.error.code ?: 500)
        } else {
            if (response.items != null) {
                val books: List<Book> = response.items.mapNotNull {
                    if (it.id != null && it.volumeInfo?.title != null
                        && it.volumeInfo.authors?.isNotEmpty() == true
                        && it.volumeInfo.publishedDate != null
                        && it.volumeInfo.language == "cs"
                    ) {
                        Book(
                            id = it.id,
                            title = it.volumeInfo.title,
                            author = it.volumeInfo.authors[0],
                            publishedDate = it.volumeInfo.publishedDate,
                            description = it.volumeInfo.description,
                            thumbnailUrl = it.volumeInfo.imageLinks?.thumbnail,
                            imageUrl = it.volumeInfo.imageLinks?.medium,
                            googlePlayLink = it.volumeInfo.infoLink,
                            webReaderLink = it.accessInfo?.webReaderLink
                        )
                    } else {
                        null
                    }
                }
                RepoBooksResponse.Success(books)
            }
            else {
                RepoBooksResponse.Error(500)
            }
        }
    }

    suspend fun getBookDetail(id: String): RepoBookDetailResponse = withContext(defaultDispatcher) {
        val response = service.getBookDetail(id)
        if (response.error != null) {
            RepoBookDetailResponse.Error(response.error.code ?: 500)
        }
        else {
            if (response.id != null && response.volumeInfo?.title != null
                && response.volumeInfo.authors?.isNotEmpty() == true
                && response.volumeInfo.publishedDate != null
            ) {
                val book = Book(
                    id = response.id,
                    title = response.volumeInfo.title,
                    author = response.volumeInfo.authors[0],
                    publishedDate = response.volumeInfo.publishedDate,
                    description = response.volumeInfo.description,
                    thumbnailUrl = response.volumeInfo.imageLinks?.thumbnail,
                    imageUrl = response.volumeInfo.imageLinks?.medium,
                    googlePlayLink = response.volumeInfo.infoLink,
                    webReaderLink = response.accessInfo?.webReaderLink
                )
                RepoBookDetailResponse.Success(book)
            } else {
                RepoBookDetailResponse.Error(500)
            }
        }
    }
}