package ru.itis.library.reposiroty.impl;

import ru.itis.library.exceptions.DbException;
import ru.itis.library.model.Book;
import ru.itis.library.reposiroty.CrudRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository implements CrudRepository<Book, Integer> {
    private final static String fileName = "data/books.csv";
    private final List<Book> data;
    private int autoincrementValue = 0;

    public BookRepository() throws DbException {
        data = readFile();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() > autoincrementValue) {
                autoincrementValue = data.get(i).getId();
            }
        }
    }

    @Override
    public Book save(Book item) throws DbException {
        if(item.getId() == null) {
            item.setId(++autoincrementValue);
            data.add(item);
            saveToFile();
            return item;
        }
        for(int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(item.getId())) {
                data.set(i, item);
                saveToFile();
                return item;
            }
        }
        throw new DbException("Book with id " + item.getId() + " not found");
    }

    @Override
    public Optional<Book> findById(Integer id) throws DbException {
        for(Book book: data) {
            if (book.getId().equals(id)) {
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Book> getAll() throws DbException {
        return data;
    }

    @Override
    public void deleteById(Integer id) throws DbException {
        for(int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(id)) {
                data.remove(i);
                saveToFile();
                return;
            }
        }
    }

    private List<Book> readFile() throws DbException {
        List<Book> result = new ArrayList<>();
        try {
            InputStream stream = new FileInputStream(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                Book book = new Book(Integer.parseInt(fields[0]), fields[1], fields[2]);
                result.add(book);
            }
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DbException("Data corrupted");
        }
        return result;
    }

    private void saveToFile() throws DbException {
        try {
            OutputStream stream = new FileOutputStream(fileName);
            PrintStream printStream = new PrintStream(stream, false, StandardCharsets.UTF_8);
            printStream.println("ID;AUTHOR;NAME");
            for (Book book : data) {
                printStream.printf("%d;%s;%s\n", book.getId(), book.getAuthor(), book.getName());
            }
            printStream.close();
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DbException("Data corrupted");
        }
    }

    public List<Book> getData() {
        return data;
    }
}
