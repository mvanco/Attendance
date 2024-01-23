package eu.matoosh.attendance.seznam.repo

import eu.matoosh.attendance.di.IoDispatcher
import eu.matoosh.attendance.seznam.api.SeznamService
import eu.matoosh.attendance.seznam.data.Book
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

sealed interface RepoBookResponse {
    data class Success(val books: List<Book>) : RepoBookResponse
    data class Error(val code: Int) : RepoBookResponse
}

class BookRepository @Inject constructor(
    private val service: SeznamService,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend fun getBooks(author: String) = flow {
        val response = service.getBooks("inauthor:$author")
        if (response.error != null) {
            emit(RepoBookResponse.Error(response.error.code ?: 500))
        } else {
            if (response.items != null) {
                val books: List<Book> = response.items.map {
                    if (it.id != null && it.volumeInfo?.title != null
                        && it.volumeInfo.authors?.isNotEmpty() == true
                        && it.volumeInfo.publishedDate != null
                    ) {
                        Book(
                            id = it.id,
                            title = it.volumeInfo.title,
                            author = it.volumeInfo.authors[0],
                            publishedDate = it.volumeInfo.publishedDate,
                            description = it.volumeInfo.description,
                            thumbnailUrl = it.volumeInfo.imageLinks?.thumbnail,
                            imageUrl = it.volumeInfo.imageLinks?.medium,
                            googlePlayLink = it.volumeInfo.accessInfo?.webReaderLink
                        )
                    } else {
                        emit(RepoBookResponse.Error(500))
                        Book("", "", "", "")
                    }
                }
                emit(RepoBookResponse.Success(books))
            }
            else {
                emit(RepoBookResponse.Error(500))
            }
        }
    }
}