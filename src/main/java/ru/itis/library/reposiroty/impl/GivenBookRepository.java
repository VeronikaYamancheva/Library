package ru.itis.library.reposiroty.impl;

import ru.itis.library.exceptions.DbException;
import ru.itis.library.model.GivenBook;
import ru.itis.library.reposiroty.CrudRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GivenBookRepository implements CrudRepository<GivenBook, Integer> {
    private final static String fileName = "data/users_books.csv";
    private final List<GivenBook> data;
    private int autoincrementValue = 0;

    public GivenBookRepository() throws DbException {
        data = readFile();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() > autoincrementValue) {
                autoincrementValue = data.get(i).getId();
            }
        }
    }

    @Override
    public GivenBook save(GivenBook item) throws DbException {
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
        throw new DbException("GivenBook with id " + item.getId() + " not found");
    }

    @Override
    public Optional<GivenBook> findById(Integer id) throws DbException {
        for(GivenBook givenBook: data) {
            if (givenBook.getId().equals(id)) {
                return Optional.of(givenBook);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<GivenBook> getAll() throws DbException {
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

    private List<GivenBook> readFile() throws DbException {
        List<GivenBook> result = new ArrayList<>();
        try {
            InputStream stream = new FileInputStream(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                GivenBook givenBook = new GivenBook(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), Integer.parseInt(fields[2]), LocalDate.parse(fields[3]));
                result.add(givenBook);
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
            printStream.println("ID;USER_ID;BOOK_ID;CREATED_DATE");
            for (GivenBook givenBook : data) {
                printStream.printf("%d;%d;%d;%s\n", givenBook.getId(), givenBook.getUserId(), givenBook.getBookId(), givenBook.getCreatedDate().toString());
            }
            printStream.close();
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DbException("Data corrupted");
        }
    }

    public List<GivenBook> getData() {
        return data;
    }
}
