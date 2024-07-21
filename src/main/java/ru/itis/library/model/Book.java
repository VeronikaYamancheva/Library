package ru.itis.library.model;

public class Book {
    private Integer id;
    private String author;
    private String name;

    public Book(Integer id, String author, String name) {
        this.id = id;
        this.author = author;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return String.format("Book, id: %d, name: %s, author: %s", id, name, author);
    }
}
