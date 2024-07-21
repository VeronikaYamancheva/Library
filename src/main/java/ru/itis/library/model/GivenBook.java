package ru.itis.library.model;

import java.time.LocalDate;

public class GivenBook {
    private Integer id;
    private Integer userId;
    private Integer bookId;
    private LocalDate createdDate;

    public GivenBook(Integer id, Integer userId, Integer bookId, LocalDate createdDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.createdDate = createdDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "GivenBook, userId: " + userId + " bookId: " + bookId + " given " + createdDate.toString();
    }
}
